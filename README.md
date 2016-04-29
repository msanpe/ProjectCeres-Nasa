# ProjectCeres

Nowadays, the drone market is exponentially growing, new users often want to fly their drone immediately after purchasing it. This creates dangerous situations as they aren’t used to flying these machines.
Our aim with Project Ceres is to avoid those dangerous situations, we do this by constantly monitoring each drone by using its GPS coordinates and keeping track of the various dangers that may occur during the flight, such as:

  -Weather problems: We take into account various weather parameters such as the wind, wind gusts or precipitations.

  -Topology: We monitor the amount of water surrounding the drone as flying over big extensions of water is dangerous, any electrical failure will mean, in most cases, a total loss of the drone.

  -Distance from the controller to the drone: Drones have a limited operation range, so we monitor this and give the user a warning if the drone is getting close to those limits.

  -Restricted areas: We monitor in real time the distance to restricted areas and give a warning if the user gets closer than 100m.

We have developed a solution using a web server which receives a GPS position from the drone, makes a request to get the weather data, processes it to get a current risk percentage and calculates a logarithmic regression to show the risk prediction for the next 15 seconds as well as the real time location of the drone.

The advantages of our solution is that we don’t need complicated drones to make it work, just a basic sensorless GPS capable drone.

Because all the calculations are done on our server, our solution is completely distributed and we accept web, desktop and mobile clients.

Creators:

Arancha Ferrero - https://github.com/aranchafoz

Dani Jimenez - https://github.com/jagerchief

Enrique Mas - https://github.com/enriquemc96

Jose Franciso Martinez Garcia - https://github.com/BroWDRock

Miguel Sancho Peña - https://github.com/msanpe

Pavel Razgovorov - https://github.com/paveltrufi

Raul Pera Pairó - https://github.com/rpairo
