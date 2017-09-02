<?php

header('cache-control: no-cache');
    
    $ch = curl_init( $_POST['url'] );     
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_HEADER, false);
    curl_setopt($ch, CURLOPT_ENCODING, "");
    curl_setopt($ch, CURLOPT_USERAGENT, "SuperFanU");
    curl_setopt($ch, CURLOPT_AUTOREFERER, 60);
    curl_setopt($ch, CURLOPT_CONNECTTIMEOUT, 60);
    curl_setopt($ch, CURLOPT_MAXREDIRS, 10);
    curl_setopt($ch, CURLOPT_SSL_VERIFYHOST , 0);
    curl_setopt($ch, CURLOPT_SSL_VERIFYPEER , false);
    curl_setopt($ch, CURLOPT_POST, true);
    curl_setopt($ch, CURLOPT_POSTFIELDS, $_POST);

    $returnText	= curl_exec( $ch );
    curl_close( $ch );

    $faqs = json_decode($returnText);

    foreach($faqs as $faq) {
        $question = $faq->faq;
        $answerURL = $faq->answer;
        echo "<div style='background-color: #fcfcfc; padding: 5px; margin-bottom: 15px; border: 2px solid #c0c0c0; border-radius: 5px;'><a href='#' class='link-track' data-url='$answerURL' style='text-decoration: none; color=#2f2f2f'>$question</a></div>";
    }
?>