<?php

require 'database.php';

class afkanerd extends database {
	private $databaseConnection;

	public function __construct() {
		$this->databaseConnection = new Database("afkanerd");
	}

	public function createNews($article) {
		$news = $this->databaseConnection->Operator("table");
		$news->set("news");

		$news->("article", $article);
	}
}


?>