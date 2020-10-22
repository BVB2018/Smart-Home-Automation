<?php

require_once 'include/DB_Functions.php';
$db = new DB_Functions();

// json response array
$response = array("error" => FALSE);

if (isset($_POST['email'])) {

    // receiving the post params
    $email = $_POST['email'];

        $user = $db->isUserExisted($email);
        if ($user) {
            // user exists 
            $response["error"] = FALSE;
            $response["user"]["email"] = $user["email"];
			=$db->getPasswordByEmail($email);
			$response["user"]["password"] = $user["password"];
			echo json_encode($response);
		
            
            
        } else {
            // user does not exist
            $response["error"] = TRUE;
            $response["error_msg"] = "Unknown error occurred in changing password!";
            echo json_encode($response);
        }
    
} else {
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameters (email) is missing!";
    echo json_encode($response);
}
?>