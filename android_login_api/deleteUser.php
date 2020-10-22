<?php

require_once 'include/DB_Functions.php';
$db = new DB_Functions();

// json response array
$response = array("error" => FALSE);

if (isset($_POST['email'])) {

    // receiving the post params
    $email = $_POST['email'];

    // check if user is existed with the email
    if ($db->isUserExisted($email)) {
        // user exists
		$user = $db->deleteUser($email);
        if ($user) {
            // user deleted successfully
            $response["error"] = FALSE;
            echo json_encode($response);
        } else {
            // user failed to delete
            $response["error"] = TRUE;
            $response["error_msg"] = "Unknown error occurred in deleting the user!";
            echo json_encode($response);
        }
        
    } else {
		$response["error"] = TRUE;
		$response["error_msg"] = "User does not exist with " . $email;
        echo json_encode($response);
    }
} else {
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameters email is missing!";
    echo json_encode($response);
}
?>

