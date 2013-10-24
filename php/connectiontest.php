<?php
/********************/
/* Konfiguroi tähän */
/********************/

//Kommentoi pois se konfikguraatio, jota ET halua käyttää ja muokkaa käyttämästäsi detaljit oikeaksi.

//Postgresql-konfiguraatio, joka toimii users:illa heittämällä
$config = array(
  'dburl' => 'pgsql:'
);
//MySql-konfiguraatio. Muista vaihtaa socketin osoitteeseen oma käyttäjänimesi, sekä tietokannan nimi ja tunnuksesi
$config = array(
  'dburl' => 'mysql:unix_socket=/home/replaceme/mysql/socket;dbname=replaceme',
  'dbusername' => 'root',
  'dbpassword' => 'replaceme',
);

/***************************************************************/
/* Koodia. Tästä tiedostosta ei kannata ottaa mallia mihinkään */
/***************************************************************/

function getDatabase() {
  global $config;
	static $db = null;
  if ($db === null) {
    $db = new PDO($config['dburl'], $config['dbusername'], $config['dbpassword']);
    $db->setAttribute(PDO::ATTR_ERRMODE,PDO::ERRMODE_EXCEPTION);
  }
  return $db;
}
function getObjects() {
  $args = func_get_args();
  $sql = array_shift($args);
  $query = getDatabase()->prepare($sql);
  $query->execute($args);
  return $query->fetchAll(PDO::FETCH_OBJ);
}
function getValue() {
  $args = func_get_args();
  $sql = array_shift($args);
  $query = getDatabase()->prepare($sql);
  $query->execute($args);
  return $query->fetchColumn();
}

class TableLister {
  protected $name;

  public function __construct($name) {$this->name = $name;}
  public function getName() { return $this->name; }

  public function getRowCount() {
    return getValue("SELECT count(*) FROM $this->name");
  }
  public function getRows($limit = 100) {
    return getObjects("SELECT * FROM $this->name LIMIT ".(int)$limit);
  }
}
class MySqlTableLister extends TableLister {
  public static function getTables() {
    $objs = getObjects("SHOW TABLES");
    $ret = array();
    foreach($objs as $obj) {
      foreach($obj as $v) {
        $ret[] = new self($v);
        break;
      }
    }
    return $ret;
  }
  public function getColumns() {
    return getObjects("DESCRIBE $this->name");
  }
}
class PgSqlTableLister extends TableLister {
  public static function getTables() {
    $objs = getObjects("select tablename from pg_tables where tableowner != 'postgres' order by tablename");
    $ret = array();
    foreach($objs as $obj) {
      $ret[] = new self($obj->tablename);
    }
    return $ret;
  }
  public function getColumns() {
    return getObjects("select column_name, data_type, is_nullable from information_schema.columns where table_name = ? and table_schema NOT IN ('information_schema', 'pg_catalog')", $this->name);
  }
}
function prettyprint($data) {
  if (count($data) == 0) {
    echo '<p>no data</p>'; 
    return;
  }
  echo '<table class="table table-striped table-bordered">';
  echo '<tr>',implode('', array_map(function($t) {return "<th>$t</th>";}, array_keys((array)array_shift(array_values($data))))),'</tr>';
  foreach($data as $datum) {
    echo '<tr>',implode('', array_map(function($t) {return "<td>$t</td>";}, (array)$datum)),'</tr>';
  }
  echo '</table>';
}

try {
  list($dbtype, $_) = explode(':', $config['dburl'], 2);
  switch($dbtype) {
    case 'mysql':
      $tables = MySqlTableLister::getTables();
       break;
    case 'pgsql':
      $tables = PgSqlTableLister::getTables();
      break;
    default:
      die("Unknown database");
  }
?>
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <title>Tietokantayhteystesti</title>
  <link rel="stylesheet" href="http://advancedkittenry.github.io/css/base.css">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.8.0/jquery.min.js"></script>
  <script src="http://advancedkittenry.github.io/javascript/bootstrap.js"></script>
  <style>.panel h2 {margin-top: -0.5em; text-transform: capitalize; } th {text-transform: capitalize;}</style>
</head>
<body> 
<div class="container" id="content">
  <h1>Projektin tietokantataulut</h1>
<?php

foreach($tables as $table): ?>
<div class='panel panel-default'><div class='panel-body'>
  <h2><?= $table->getName(); ?></h2>
  <?php prettyprint($table->getColumns()); ?>
  <button type='button' class='btn-link expandable collapsed' data-toggle='collapse' data-target='#expandable_<?= $table->getName(); ?>'>
  Yhteensä <?= $table->getRowCount(); ?> riviä:
  </button>
  <div id='expandable_<?= $table->getName(); ?>' class='collapse'>
  <?php prettyprint($table->getRows()); ?>
  </div>
</div></div>
<?php endforeach; ?>
</div>
</body>
</html><?php } catch (Exception $e) {
  echo '<pre>', $e;
}
