<?php

require_once 'include/DB_Functions.php';
$db = new DB_Functions();

// json response array
$response = array("error" => FALSE);

if (isset($_POST['email']) && isset($_POST['curr_password']) && isset($_POST['new_password'])) {

    // receiving the post params
    $email = $_POST['email'];
    $curr_password = $_POST['curr_password'];
    $new_password = $_POST['new_password'];

        $user = $db->updatePassword($email, $curr_password, $new_password);
        if ($user) {
            // user stored successfully
            $response["error"] = FALSE;
       
            $response["user"]["email"] = $user["email"];
            $response["user"]["curr_password"] = $user["curr_password"];
            $response["user"]["new_password"] = $user["new_password"];
            
            echo json_encode($response);
        } else {
            // user failed to store
            $response["error"] = TRUE;
            $response["error_msg"] = "Unknown error occurred in changing password!";
            echo json_encode($response);
        }
    
} else {
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameters (email, current password or new password) is missing!";
    echo json_encode($response);
}
?>