<?php

namespace RestBundle\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\Controller;
use Symfony\Component\HttpFoundation\Request;


class DefaultController extends Controller {
    public function checkAction(Request $request) {
      header('Access-Control-Allow-Origin: *');
      $lat = $request->get('lat');
      $lng = $request->get('lng');
      $olat = $request->get('olat');
      $olng = $request->get('olng');
      $resources = $this->get('resources_helper');

      $response = $resources->decideResponse($lat, $lng, $olat, $olng);

      echo json_encode($response);
      die;
    }
    public function setLocationAction(Request $request) {
      $location = $request->get('lat') . "," . $request->get('lng');
      file_put_contents('coords.txt', $location);
      echo 1;
      die;
    }

    public function getLocationAction() {
      header('Access-Control-Allow-Origin: *');
      $read_file = explode(",",file_get_contents('coords.txt'));
      $response = array (
        'lat' => floatval($read_file[0]),
        'lng' => floatval($read_file[1])
      );
      echo json_encode($response);
      die;
    }

    public function checkBeforeAction(Request $request) {
      $lat = $request->get('lat');
      $lng = $request->get('lng');
      $resources = $this->get('resources_helper');

      $res_area = $resources->restricted_area($lat, $lng);
      $weather = $resources->weather($lat, $lng);
      $weatherData = $resources->weatherData($lat, $lng);
      $water = $resources->waterPercentage($lat, $lng, 13);
      $decision = $resources->takeoffDecision($res_area, $weather, $water);
      $response = array(
        'res_area' => $res_area,
        'weather' => $weatherData,
        'water' => $water,
        'takeoff' => !$decision
      );

      echo json_encode($response);
      die;
    }
}
