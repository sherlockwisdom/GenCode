<?php


$database = $argv[2];
$command = $argv[3];
$value = $argv[4];
if($argc == 6)
	$table = $argv[5];

//	arguments: --database db_name --command query table

echo("_php_script: [db] $database  [cm] $command [vl] $value [tb] $table");

$servername = "localhost";
$username = "root";
$password = "asshole";

// // Create connection
$conn = new mysqli($servername, $username, $password);

// //Check connection
// if ($conn->connect_error) {
//    die("Connection failed: " . $conn->connect_error);
// }

switch($command) {
	case "--is_database":
	$query = "SHOW DATABASES LIKE '$database'";
	$result = $conn->query($query) or die($conn->error);
	// echo "_tmp_message: ".sizeof($result)." \n";
	// foreach($result as $value) {
	// 	echo "value_type: ".gettype($value)."\n";
	// }
	if($result->num_rows > 0)
		echo "true";
	else
		echo "false";
	break;

	case "--make":
	$query = "CREATE DATABASE IF NOT EXISTS $database";
	$result = $conn->query($query) or die($conn->error);
	if($result === TRUE) 
		echo "true";
	else
		echo "false";
	break;

	case "--is_table":
	$conn = new mysqli($servername, $username, $password, $database);

	$query = "SHOW TABLES LIKE '".$value."'";
	$result = $conn->query($query) or die($conn->error);
	if($result->num_rows > 0) 
		echo "true";
	else 
		echo "false";
	break;


	case "--create_table":
	$conn = new mysqli($servername, $username, $password, $database);

	$query = "CREATE TABLE $value ( 
		id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY
	)";
	$result = $conn->query($query) or die($conn->error);
	if($result === TRUE) 
		echo "true";
	else 
		echo "false";
	break;


	case "--is_field":
	$conn = new mysqli($servername, $username, $password, $database);

	$query = "SELECT * FROM $value";
	$results = $conn->query($query) or die($conn->error);
	if($results->num_rows > 0) {
		$rows = $results->fetch_assoc();
		if(isset($rows[$field])) {
			echo "true";
		}
		else 
			echo "false";
	} 
	else 
		echo "false";
	break;


	case "--create_field":
	$type_list = array("i"=>"INT", "str"=>"CHAR(255)", "date"=>"date"); //TODO expand list to be more extensive
	$conn = new mysqli($servername, $username, $password, $database);
	$vals = explode('.', $value);
	$type = $type_list[$vals[0]];
	$query = "ALTER TABLE $table ADD COLUMN ".$vals[1]." $type";
	$results = $conn->query($query) or die($conn->error);
	if($results === TRUE) 
		echo "true";
	else 
		echo "false";
	break;
}

?>