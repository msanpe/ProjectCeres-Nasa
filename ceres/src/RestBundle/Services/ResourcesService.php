<?php

namespace RestBundle\Services;
use PolynomialRegression\LinearizedRegression\LogRegression;
use PolynomialRegression\LinearizedRegression\PolynomialRegression;
use PolynomialRegression\LinearizedRegression\ExpRegression;
/*
* This service aims to deal with booking-related operations
º*/

class ResourcesService {
  public function decideResponse($lat, $lon, $olat, $olon) {
    $RTres_area = $this->restricted_area($lat, $lon);
    $RTweatherData = $this->weatherData($lat, $lon);
    $RTweather = $this->weather($lat, $lon);
    $RTdistance = $this->wentAway($lat, $lon, $olat, $olon);
    $RTdisR = $this->distanceToRestriction($RTres_area);
    $RTwater = $this->waterPercentage($lat, $lon, 17);

    /*echo "AREA: $RTres_area<br />
          WEATHER: $RTweather<br />
          DISTANCE: $RTdisR<br />
          WATER: $RTwater<br />";*/

    //Decisiones de rangos
    $RTdecision = 1;
    if($RTdisR < 0.9 && $RTwater < 0.9 && $RTdistance < 0.9 && $RTweather < 0.9) {
      if($RTdisR <= 0 && $RTwater >= 0.3) $RTdecision = $RTwater * 0.15 + $RTweather * 0.4 + $RTdistance * 0.45;
      else if($RTdisR <= 0 && $RTwater < 0.3) $RTdecision = $RTweather * 0.45 + $RTdistance * 0.55;
      else if($RTdisR > 0 && $RTwater < 0.3) $RTdecision = $RTweather * 0.3 + $RTdisR * 0.35 + $RTdistance * 0.35;
      else $RTdecision = $RTweather * 0.27 + $RTdisR * 0.3 + $RTdistance * 0.32 + $RTwater * 0.15;
    }

    $this->saveRTDecision($RTdecision);
    $RTprediction = $this->predictAvail();

    return array(
      'weatherData' => $RTweatherData,
      'risk' => $RTdecision,
      'prediction' => $RTprediction
    );
  }

  public function waterPercentage($lat, $lon, $zoom) {
    $im = \imagecreatefrompng("http://maps.googleapis.com/maps/api/staticmap?scale=2&center=$lat,$lon&zoom=$zoom&size=400x400&sensor=false&visual_refresh=true&style=feature:water|color:0x00FF00&style=element:labels|visibility:off&style=feature:transit|visibility:off&style=feature:poi|visibility:off&style=feature:road|visibility:off&style=feature:administrative|visibility:off");
    $width = imagesx($im);
    $height = imagesy($im);
    $sum = 0;

    $index_color = imagecolorat($im, $width/2, $height/2);
    $colors = imagecolorsforindex($im, $index_color);
    //if($colors['red'] < 20 && $colors['green'] > 240 && $colors['blue'] < 20) return 1;

    for ($i=0; $i < $width; $i++) {
      for ($j=0; $j < $height; $j++) {
        $index_color = imagecolorat($im, $i, $j);
        $colors = imagecolorsforindex($im, $index_color);
        if($colors['red'] < 20 && $colors['green'] > 240 && $colors['blue'] < 20) $sum++;
      }
    }
    $res = (($sum)/($width*$height))*1.5;
    return $res > 1 ? 1 : $res;
  }

  public function insidePolygon($polygon,$point) {
    /*$polygon2 = array(
      array(25.774,-80.190),
      array(18.466,-66.118),
      array(32.321,-64.757)
    );
    var_dump($polygon, $polygon2);*/
    $corners = sizeof($polygon);
    $i = sizeof($polygon)-1;
    $j = $i;
    $contains = false;
    for ($i=0; $i<$corners; $i++) {
      if (($polygon[$i][1] < $point[1] && $polygon[$j][1]>= $point[1]) || ($polygon[$j][1]<$point[1] && $polygon[$i][1]>=$point[1])) {
        if ($polygon[$i][0] + ($point[1] - $polygon[$i][1])/($polygon[$j][1] - $polygon[$i][1])*($polygon[$j][0] - $polygon[$i][0]) < $point[0]) {
          $contains=!$contains;
        }
      }
      $j=$i;
    }
    return $contains;
  }

  public function weather($lat, $lon) {
    $json_string = file_get_contents("http://api.wunderground.com/api/f3390a71f32089db/conditions/q/".$lat.",".$lon.".json");
    $parsed_json = json_decode($json_string);
    $temp = $parsed_json->{'current_observation'}->{'temp_c'};
    $wind_speed = $parsed_json->{'current_observation'}->{'wind_kph'};
    $wind_gust = $parsed_json->{'current_observation'}->{'wind_gust_kph'};
    $rain = $parsed_json->{'current_observation'}->{'precip_today_metric'};
    $percentage = (($wind_speed/35)+($wind_gust/40))/2;
    return $percentage;
  }

  public function weatherData($lat, $lon) {
    $json_string = file_get_contents("http://api.wunderground.com/api/f3390a71f32089db/conditions/q/".$lat.",".$lon.".json");
    $parsed_json = json_decode($json_string);
    $temp = $parsed_json->{'current_observation'}->{'temp_c'};
    $wind_speed = $parsed_json->{'current_observation'}->{'wind_kph'};
    $wind_gust = $parsed_json->{'current_observation'}->{'wind_gust_kph'};
    $rain = $parsed_json->{'current_observation'}->{'precip_today_metric'};
    $response = array(
      'temp' => $temp,
      'windSpeed' => $wind_speed,
      'windGust' => $wind_gust,
      'rain'	=> $rain
    );

    return $response;
  }

  public function wentAway($latd, $lond, $latc, $lonc){
    # $maxaway = 90
    # this class works for a maximum distance of 90 m
    $theta = $lond - $lonc;
    $dist = sin(deg2rad($latd)) * sin(deg2rad($latc)) + cos(deg2rad($latd)) * cos(deg2rad($latc)) * cos(deg2rad($theta));
    $dist = acos($dist);
    $dist = rad2deg($dist);
    $meters = ($dist * 60 * 1.1515) / 1609.34;
    $exp = 0;
    if($meters < 70){
      $exp = (0.3 * $meters) / 70;
    } elseif($meters >= 70 && $meters < 89){
      $exp = 0.3 + 0.035 * ($meters - 70);
    } else{
      $exp = 0.96 + 0.4 * ($meters - 89);
    }
    return $exp * 100;
  }

  public function takeoffDecision($res_area, $weather, $water) {
    return $res_area < 8 || $weather == 1 || $water > 0.4 ? 1 : 0;
  }

  public function predictAvail(){
    $file = file('prediction.txt');
    $lines = sizeof($file);
    if($lines >= 10) {
      $regression = new PolynomialRegression(2);

      foreach($file as $key=>$line) {
        $value = (float)$line*1000;
        $regression->addData( $key, $value);
      }
      $coefficients = $regression->getCoefficients();

      $predictionNum = 5;
      $predictions = array();
      for($i=1;$i<=$predictionNum;$i++){
        $y = abs(($regression->interpolate( $coefficients, $lines+$i ))/1000);
        if($y > 1) $y = 1;
        array_push($predictions,$y);
      }
      return json_encode($predictions);
    }
    else {
      return ;
    }
  }

  public function flyingDecision($RTres_area,$weather, $distance, $water) {
    return $weather == 1 || $distance == 1 || $water > 0.7 ? 1 : $weather * 0.05 + $distance * 0.15 + $water * 0.05 + $RTres_area * 0.75;
  }

  public function saveRTDecision($RTdecision) {
    $file = file('prediction.txt');
    $lines = count($file);
    $txt = "$RTdecision";
    if($lines >= 10) {
      unset($file[0]);
      file_put_contents('prediction.txt', $file);
    }
    file_put_contents('prediction.txt', $txt.PHP_EOL , FILE_APPEND);
  }

  public function process_line($line) {
    $coords = explode("+", $line);
    for ($i=0; $i < sizeof($coords); $i++) {
      $pair = explode(",", $coords[$i]);
      $polygon[$i][0] = floatval($pair[0]);
      $polygon[$i][1] = floatval($pair[1]);
    }

    return $polygon;
  }

  public function distance_between_points($p, $q) {
    return sqrt(pow($p[0]-$q[0], 2) + pow($p[1]-$q[1], 2));
  }

  public function distAB($lat_a, $lon_a, $lat_b, $lon_b) {
    $earth_radius = 6372.795477598;
    $delta_lat = $lat_b - $lat_a ;
    $delta_lon = $lon_b - $lon_a ;

    $alpha    = $delta_lat/2;
    $beta     = $delta_lon/2;
    $a        = sin(deg2rad($alpha)) * sin(deg2rad($alpha)) + cos(deg2rad($lat_a)) * cos(deg2rad($lat_b)) * sin(deg2rad($beta)) * sin(deg2rad($beta)) ;
    $c        = asin(min(1, sqrt($a)));
    $distance = 2*$earth_radius * $c;
    $distance = round($distance, 4);

    return $distance;
  }

  public function min_distance_polygon_point($polygon, $point) {
    $ret = $min = INF;
    for ($i=0; $i < sizeof($polygon); $i++) {
      $dist = $this->distance_between_points($polygon[$i], $point);
      if($dist < $min) {
        $min = $dist;
        $ret = $this->distAB($point[0], $point[1], $polygon[$i][0], $polygon[$i][1]);
      }
    }
    return $ret;
  }

  public function restricted_area($lat, $lng) {
    $handle = fopen("res.txt", "r");
    if ($handle) {
      for ($i = 0; ($line = fgets($handle)) !== false; $i++) {
        $polygons[$i] = $this->process_line($line);
      }
      fclose($handle);
    }

    $min = INF;
    for($i = 0; $i < sizeof($polygons); $i++) {
      $origin_point[0] = floatval($lat);
      $origin_point[1] = floatval($lng);
      $distance = $this->min_distance_polygon_point($polygons[$i], $origin_point);
      if($distance < $min) $min = $distance;
    }
    return $min;
  }

  public function distanceToRestriction($RTres_area) {
    $percentage = 0;
    if ($RTres_area < 8) {
      if ($RTres_area < 0.1) {
        $percentage = 1;
      } else {
        $percentage = ($RTres_area - 8) * (1/7.9);
      }
    }
    return $percentage;
  }
}
