<?php

require_once 'include/DB_Functions.php';
$db = new DB_Functions();

// json response array
$response = array("error" => FALSE);

if (true) {

        $user = $db->getActivitiesData();
        if ($user) {
            // data stored successfully
            $response["error"] = FALSE;
       
            $response["user"]["light"] = $user["light"];
            $response["user"]["fan"] = $user["fan"];
            $response["user"]["door"] = $user["door"];
            
            echo json_encode($response);
        } else {
            // data failed to store
            $response["error"] = TRUE;
            $response["error_msg"] = "Unknown error occurred in updation!";
            echo json_encode($response);
        }
    
} else {
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameters (light, fan or door) is missing!";
    echo json_encode($response);
}
?>