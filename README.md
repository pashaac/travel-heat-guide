**TREII**

*Travel and Real estate industries intercommunication*

Backend part

1. Install gradle (build system)
2. install postgresql (https://www.digitalocean.com/community/tutorials/how-to-install-and-use-postgresql-on-ubuntu-16-04)
2.1. create database 'treii' (you can find it in manual above)

2.2. change password on 'postgres' (https://stackoverflow.com/questions/12720967/how-to-change-postgresql-user-password)

Now you can start backend part like 'gradle bootRun'

UI part

go to src/main/web (you can find index/main/css and bower.json)
3.1. next steps for install NodeJS environment

sudo apt-get install nodejs
sudo apt-get install npm
npm install -g bower
bower install

3.2. go to index.html from IDEA and click on browser icon at right-up corner

Rest call

* /attraction?lat={}&lng={}
* /attraction/remove?lat={}&lng={}
* /attraction/force?lat={}&lng={}


* /nightLifeSpots?lat={}&lng={}
* /nightLifeSpots/remove?lat={}&lng={}
* /nightLifeSpots/force?lat={}&lng={}

