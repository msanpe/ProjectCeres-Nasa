<?php
function takeoffDecision($res_area, $weather, $water) {
  return $res_area == 1 || $weather == 1 || $water > 0.7 ? 1 : $weather * 0.875 + $water * 0.125;
}

echo takeoffDecision(0,0.36,0.7);
echo "\n";
?>
