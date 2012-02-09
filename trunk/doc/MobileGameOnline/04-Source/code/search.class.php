<?php
defined('_PPCLink') or die('Restricted access');

class Search{
function makeStrSearch($searchPattern){	//return STRING
	switch($searchPattern["version"]){
		case 1:{	//version 1
			$str = $searchPattern["version"] .SPF_SEPARATE. $searchPattern["username"] .SPF_SEPARATE. $searchPattern["message"];
			break;
		}
		case 2:{
			break;
		}
		default:{
			break;
		}
	}
	return $str;
}
function parseSearch($str){	//return ARRAY
	$tmp_arr = split(SPF_SEPARATE, $str);
	
	$searchPattern["version"] = $tmp_arr[0];
	switch($searchPattern["version"]){
		case 1:{	//version 1
			$searchPattern["username"] = $tmp_arr[1];
			$searchPattern["message"] = $tmp_arr[2];
			break;
		}
		case 2:{
			break;
		}
		default:{
			break;
		}
	}
	return $searchPattern;
}
}
?>