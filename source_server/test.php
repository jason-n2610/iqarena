<?php

$email = "abcdef@gmail.com";
list($Username, $Domain) = preg_split("@",$email);
if(getmxrr($Domain, $MXHost))
{
    echo "true";
   return TRUE;
}
else
{
   if(fsockopen($Domain, 25, $errno, $errstr, 30))
   {
        echo "true";
      return TRUE;
   }
   else
   {
        echo "false";
      return FALSE;
   }
}
?>