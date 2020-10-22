<?php

class DB_Functions {

    private $conn;

    // constructor
    function __construct() {
        require_once 'DB_Connect.php';
        // connecting to database
        $db = new Db_Connect();
        $this->conn = $db->connect();
    }

    // destructor
    function __destruct() {
        
    }

    /**
     * Storing new user
     * returns user details
     */
    public function storeUser($name, $email, $password, $phone ,$category) {
        $uuid = uniqid('', true);
        $hash = $this->hashSSHA($password);
        $encrypted_password = $hash["encrypted"]; // encrypted password
        $salt = $hash["salt"]; // salt

        $stmt = $this->conn->prepare("INSERT INTO users(unique_id, name, email, encrypted_password,phone,category, salt, created_at) VALUES(?, ?, ?, ?, ?, ?, ?, NOW())");
        $stmt->bind_param("sssssss", $uuid, $name, $email, $encrypted_password,$phone,$category, $salt);
        $result = $stmt->execute();
        $stmt->close();

        // check for successful store
        if ($result) {
            $stmt = $this->conn->prepare("SELECT * FROM users WHERE email = ?");
            $stmt->bind_param("s", $email);
            $stmt->execute();
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();

            return $user;
        } else {
            return false;
        }
    }
	
	/**
     * Storing user in log history table
     */
	public function storeUserLogTable($email) {
        $stmt = $this->conn->prepare("INSERT INTO log_history(email, log_in_time) VALUES(?, NOW())");
        $stmt->bind_param("s",$email);
        $result = $stmt->execute();
        $stmt->close();
        // check for successful store
        if ($result) {
			return true;
        } else {
            return false;
        }
    }
	
	/**
     * Updating acivity table
     */
	public function updateActivity($light, $fan, $door) {

        $stmt = $this->conn->prepare("UPDATE activity SET light=? , fan=? , door=?");
        $stmt->bind_param("sss", $light, $fan, $door);
        $result = $stmt->execute();
        $stmt->close();
        // check for successful store
        if ($result) {
            return true;
        } else {
            return false;
        }
    }
	
	/**
     * Change password
     */
	public function updatePassword($email, $current_password, $new_password) {
		$stmt = $this->conn->prepare("SELECT * FROM users WHERE email = ?");
        $stmt->bind_param("s", $email);
		
		if ($stmt->execute()) {
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();

            // verifying user password
            $salt = $user['salt'];
            $encrypted_password = $user['encrypted_password'];
            $hash = $this->checkhashSSHA($salt, $current_password);
            // check for password equality
            if ($encrypted_password == $hash) {
				$hash = $this->hashSSHA($new_password);
				$encrypted_password = $hash["encrypted"]; // encrypted password
				$salt = $hash["salt"]; // salt
				$stmt = $this->conn->prepare("UPDATE users SET encrypted_password=? , salt=? WHERE email=?");
				$stmt->bind_param("sss", $encrypted_password, $salt, $email);
				$result = $stmt->execute();
				$stmt->close();
            }
        } 
        // check for success
        if ($result) {
            return true;
        } else {
            return false;
        }
    }
	
    /**
     * Get user by email and password
     */
    public function getUserByEmailAndPassword($email, $password) {

        $stmt = $this->conn->prepare("SELECT * FROM users WHERE email = ?");

        $stmt->bind_param("s", $email);

        if ($stmt->execute()) {
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();

            // verifying user password
            $salt = $user['salt'];
            $encrypted_password = $user['encrypted_password'];
            $hash = $this->checkhashSSHA($salt, $password);
            // check for password equality
            if ($encrypted_password == $hash) {
                // user authentication details are correct
                return $user;
            }
        } else {
            return NULL;
        }
    }
	/**
     * Get activities data
     */
	public function getActivitiesData(){
		$stmt=$this->conn->prepare("SELECT * FROM activity");
		$result = $stmt->execute();
		if ($result) {
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();
            return $user;
        } else {
            return false;
        }
		
	}
	
	/**
     * Get user by email 
     */
	public function getUserByEmail($email) {
		$stmt=$this->conn->prepare("SELECT * FROM users WHERE email = ?");
		$stmt->bind_param("s", $email);
		if ($stmt->execute()) {
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();
			
			
			// verifying user password
            $salt = $user['salt'];
            $encrypted_password = $user['encrypted_password'];
			$result=base64_decode($encrypted_password);
			
            
            return $result;
        } else {
            return NULL;
        }
    }

	/**
     * Delete user
     */
	public function deleteUser($email) {
		$stmt=$this->conn->prepare("DELETE FROM users WHERE email = ?");
		$stmt->bind_param("s", $email);
		if ($stmt->execute()) { 
            return true;
        } else {
            return false;
        }
    }
	
	
    /**
     * Check user is existed or not
     */
    public function isUserExisted($email) {
        $stmt = $this->conn->prepare("SELECT email from users WHERE email = ?");

        $stmt->bind_param("s", $email);

        $stmt->execute();

        $stmt->store_result();

        if ($stmt->num_rows > 0) {
            // user existed 
            $stmt->close();
            return true;
        } else {
            // user not existed
            $stmt->close();
            return false;
        }
    }

    /**
     * Encrypting password
     * @param password
     * returns salt and encrypted password
     */
    public function hashSSHA($password) {

        $salt = sha1(rand());
        $salt = substr($salt, 0, 10);
        $encrypted = base64_encode(sha1($password . $salt, true) . $salt);
        $hash = array("salt" => $salt, "encrypted" => $encrypted);
        return $hash;
    }

    /**
     * Decrypting password
     * @param salt, password
     * returns hash string
     */
    public function checkhashSSHA($salt, $password) {

        $hash = base64_encode(sha1($password . $salt, true) . $salt);

        return $hash;
    }

}

?>
