# Bluefever

Bluefever is a heating control application, which has currently two WebGUI. The first [one](/Bluefever/Bluefever-Gui/) is the more "complete" one, but it's written in Java with ZK and really slow on a Odroid X4.

## Requirements
- [Java 1.8](https://java.com/de/download/)
- [MariaDB 5.5](https://mariadb.com/downloads/)
- [Maven](https://maven.apache.org/download.cgi)
- Java GUI: [Tomcat 9](https://tomcat.apache.org/download-90.cgi)
- PHP Gui: Apache 2.4.18 + PHP 7.0.32
- [Bluez 5.37](http://www.bluez.org/)
- USB Bluetooth 4.0 USB Mini Adapter (CSR 8510 A10)
- [Xavax Wireless Radiator Control, Bluetooth](https://www.xavax.eu/00111971/xavax-wireless-radiator-control-bluetooth)

## Notes
All uuids & handle mappings are reverse engineered from the [XAVAX App](https://play.google.com/stor/apps/details?id=com.mobifyi.xavax_app) in Google Play Store. The app was [converted](https://github.com/pxb1988/dex2jar) to jar and the class files were [decompiled](http://jd.benow.ca/).

# Setup & MAC address
1) Install bluez
   ```
   sudo apt-get install bluez
   ```
2) Scan devices
   ```
   sudo hcitool lescan
   
   Output should look like:
   78:A5:04:37:77:XX Comet Blue
   54:4A:16:5D:13:XX Comet Blue
   78:A5:04:37:03:XX Comet Blue
   ...
   ```

# Bluez commands
- Get all handles:
  ```
  sudo gatttool -b 54:4A:16:5D:13:XX -- char-desc
  ```
  <details><summary>All handles</summary><ul>
  <li>handle: 0x0001, uuid: 00002800-0000-1000-8000-00805f9b34fb</li>
  <li>handle: 0x0002, uuid: 00002803-0000-1000-8000-00805f9b34fb</li>
  <li>handle: 0x0003, uuid: 00002a00-0000-1000-8000-00805f9b34fb</li>
  <li>handle: 0x0004, uuid: 00002803-0000-1000-8000-00805f9b34fb</li>
  <li>handle: 0x0005, uuid: 00002a01-0000-1000-8000-00805f9b34fb</li>
  <li>handle: 0x0006, uuid: 00002803-0000-1000-8000-00805f9b34fb</li>
  <li>handle: 0x0007, uuid: 00002a02-0000-1000-8000-00805f9b34fb</li>
  <li>handle: 0x0008, uuid: 00002803-0000-1000-8000-00805f9b34fb</li>
  <li>handle: 0x0009, uuid: 00002a03-0000-1000-8000-00805f9b34fb</li>
  <li>handle: 0x000a, uuid: 00002803-0000-1000-8000-00805f9b34fb</li>
  <li>handle: 0x000b, uuid: 00002a04-0000-1000-8000-00805f9b34fb</li>
  <li>handle: 0x000c, uuid: 00002800-0000-1000-8000-00805f9b34fb</li>
  <li>handle: 0x000d, uuid: 00002803-0000-1000-8000-00805f9b34fb</li>
  <li>handle: 0x000e, uuid: 00002a05-0000-1000-8000-00805f9b34fb</li>
  <li>handle: 0x000f, uuid: 00002902-0000-1000-8000-00805f9b34fb</li>
  <li>handle: 0x0010, uuid: 00002800-0000-1000-8000-00805f9b34fb</li>
  <li>handle: 0x0011, uuid: 00002803-0000-1000-8000-00805f9b34fb</li>
  <li>handle: 0x0012, uuid: 00002a23-0000-1000-8000-00805f9b34fb</li>
  <li>handle: 0x0013, uuid: 00002803-0000-1000-8000-00805f9b34fb</li>
  <li>handle: 0x0014, uuid: 00002a24-0000-1000-8000-00805f9b34fb</li>
  <li>handle: 0x0015, uuid: 00002803-0000-1000-8000-00805f9b34fb</li>
  <li>handle: 0x0016, uuid: 00002a26-0000-1000-8000-00805f9b34fb</li>
  <li>handle: 0x0017, uuid: 00002803-0000-1000-8000-00805f9b34fb</li>
  <li>handle: 0x0018, uuid: 00002a28-0000-1000-8000-00805f9b34fb</li>
  <li>handle: 0x0019, uuid: 00002803-0000-1000-8000-00805f9b34fb</li>
  <li>handle: 0x001a, uuid: 00002a29-0000-1000-8000-00805f9b34fb</li>
  <li>handle: 0x001b, uuid: 00002800-0000-1000-8000-00805f9b34fb</li>
  <li>handle: 0x001c, uuid: 00002803-0000-1000-8000-00805f9b34fb</li>
  <li>handle: 0x001d, uuid: 47e9ee01-47e9-11e4-8939-164230d1df67</li>
  <li>handle: 0x001e, uuid: 00002803-0000-1000-8000-00805f9b34fb</li>
  <li>handle: 0x001f, uuid: 47e9ee10-47e9-11e4-8939-164230d1df67</li>
  <li>handle: 0x0020, uuid: 00002803-0000-1000-8000-00805f9b34fb</li>
  <li>handle: 0x0021, uuid: 47e9ee11-47e9-11e4-8939-164230d1df67</li>
  <li>handle: 0x0022, uuid: 00002803-0000-1000-8000-00805f9b34fb</li>
  <li>handle: 0x0023, uuid: 47e9ee12-47e9-11e4-8939-164230d1df67</li>
  <li>handle: 0x0024, uuid: 00002803-0000-1000-8000-00805f9b34fb</li>
  <li>handle: 0x0025, uuid: 47e9ee13-47e9-11e4-8939-164230d1df67</li>
  <li>handle: 0x0026, uuid: 00002803-0000-1000-8000-00805f9b34fb</li>
  <li>handle: 0x0027, uuid: 47e9ee14-47e9-11e4-8939-164230d1df67</li>
  <li>handle: 0x0028, uuid: 00002803-0000-1000-8000-00805f9b34fb</li>
  <li>handle: 0x0029, uuid: 47e9ee15-47e9-11e4-8939-164230d1df67</li>
  <li>handle: 0x002a, uuid: 00002803-0000-1000-8000-00805f9b34fb</li>
  <li>handle: 0x002b, uuid: 47e9ee16-47e9-11e4-8939-164230d1df67</li>
  <li>handle: 0x002c, uuid: 00002803-0000-1000-8000-00805f9b34fb</li>
  <li>handle: 0x002d, uuid: 47e9ee20-47e9-11e4-8939-164230d1df67</li>
  <li>handle: 0x002e, uuid: 00002803-0000-1000-8000-00805f9b34fb</li>
  <li>handle: 0x002f, uuid: 47e9ee21-47e9-11e4-8939-164230d1df67</li>
  <li>handle: 0x0030, uuid: 00002803-0000-1000-8000-00805f9b34fb</li>
  <li>handle: 0x0031, uuid: 47e9ee22-47e9-11e4-8939-164230d1df67</li>
  <li>handle: 0x0032, uuid: 00002803-0000-1000-8000-00805f9b34fb</li>
  <li>handle: 0x0033, uuid: 47e9ee23-47e9-11e4-8939-164230d1df67</li>
  <li>handle: 0x0034, uuid: 00002803-0000-1000-8000-00805f9b34fb</li>
  <li>handle: 0x0035, uuid: 47e9ee24-47e9-11e4-8939-164230d1df67</li>
  <li>handle: 0x0036, uuid: 00002803-0000-1000-8000-00805f9b34fb</li>
  <li>handle: 0x0037, uuid: 47e9ee25-47e9-11e4-8939-164230d1df67</li>
  <li>handle: 0x0038, uuid: 00002803-0000-1000-8000-00805f9b34fb</li>
  <li>handle: 0x0039, uuid: 47e9ee26-47e9-11e4-8939-164230d1df67</li>
  <li>handle: 0x003a, uuid: 00002803-0000-1000-8000-00805f9b34fb</li>
  <li>handle: 0x003b, uuid: 47e9ee27-47e9-11e4-8939-164230d1df67</li>
  <li>handle: 0x003c, uuid: 00002803-0000-1000-8000-00805f9b34fb</li>
  <li>handle: 0x003d, uuid: 47e9ee2a-47e9-11e4-8939-164230d1df67</li>
  <li>handle: 0x003e, uuid: 00002803-0000-1000-8000-00805f9b34fb</li>
  <li>handle: 0x003f, uuid: 47e9ee2b-47e9-11e4-8939-164230d1df67</li>
  <li>handle: 0x0040, uuid: 00002803-0000-1000-8000-00805f9b34fb</li>
  <li>handle: 0x0041, uuid: 47e9ee2c-47e9-11e4-8939-164230d1df67</li>
  <li>handle: 0x0042, uuid: 00002803-0000-1000-8000-00805f9b34fb</li>
  <li>handle: 0x0043, uuid: 47e9ee2d-47e9-11e4-8939-164230d1df67</li>
  <li>handle: 0x0044, uuid: 00002803-0000-1000-8000-00805f9b34fb</li>
  <li>handle: 0x0045, uuid: 47e9ee2e-47e9-11e4-8939-164230d1df67</li>
  <li>handle: 0x0046, uuid: 00002803-0000-1000-8000-00805f9b34fb</li>
  <li>handle: 0x0047, uuid: 47e9ee30-47e9-11e4-8939-164230d1df67</li>
  </ul>
  </details>
  
- Read handle:
  ```
  sudo gatttool -b 54:4A:16:5D:13:XX --char-read -a 0x0018
  > Characteristic value/descriptor: 30 2e 30 2e 34
  ```
- Authenticate & set Temperature
  ```
  sudo gatttool -b 54:4A:16:5D:13:XX --char-write-req -a 0x0047 -n 00000000;
  sleep 1;
  sudo gatttool -b 54:4A:16:5D:13:XX --char-write-req -a 0x003f -n 80268080008080;
  ```
  [Password Encoder](/Bluefever/Bluefever-Shared/src/main/java/com/helloingob/bluefever/command/encoder/PasswordEncoder.java) &   [Temperature Encoder](/Bluefever/Bluefever-Shared/src/main/java/com/helloingob/bluefever/command/encoder/TemperatureCoder.java)

## Build
  - Edit [Settings.java](/Bluefever/Bluefever-Shared/src/main/java/com/helloingob/bluefever/Settings.java) for logfilepath
  - Run "**mvn clean package**"
  
## Database
Add user "**bluefever**" with "**helloingob**" password & create **database**.

  ```
  CREATE USER 'bluefever'@'localhost' IDENTIFIED BY 'helloingob';
  GRANT ALL PRIVILEGES ON bluefever.* TO 'bluefever'@'localhost';
  FLUSH PRIVILEGES;

  CREATE DATABASE bluefever;  
  ```
Execute [schema.sql](/Bluefever/Bluefever-Shared/sql/schema.sql)

## Setup Java Gui:
1) Copy bluefever.war file to Tomcat webapps directory
2) Start Tomcat server
3) Access http://localhost:8080/bluefever

## Setup PHP Gui:
1) Install [apache](https://www.raspberrypi.org/documentation/remote-access/web-server/apache.md)
2) Install PHP 7
3) Copy content of the [PHP Gui](/Bluefever-PHP/) folder in "/var/www/html" on raspberry pi
4) Finish! The GUI should be accessible on Port 80

## Demonstration
- [PHP Web Gui](/img/bluefever-gui-php.jpg?raw=true)
