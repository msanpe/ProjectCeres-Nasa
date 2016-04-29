sudo apt-get update
sudo apt-get install -y python-software-properties
sudo add-apt-repository ppa:ondrej/php5
sudo apt-get update
sudo apt-get install -y php5
sudo apt-get install -y libapache2-mod-php5
sudo apt-get install -y php5-mysql
sudo a2enmod rewrite
sudo apt-get install php5-curl
sed -i -e 's/www-data/vagrant/g' /etc/apache2/envvars
sudo service apache2 restart
