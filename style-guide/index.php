<?php
  // Build out URI to reload from form dropdown
  // Need full url for this to work in Opera Mini
  $pageURL = (@$_SERVER["HTTPS"] == "on") ? "https://" : "http://";

  if (isset($_POST['sg_uri']) && isset($_POST['sg_section_switcher'])) {
     $pageURL .= $_POST[sg_uri].$_POST[sg_section_switcher];
     $pageURL = htmlspecialchars( filter_var( $pageURL, FILTER_SANITIZE_URL ) );
     header("Location: $pageURL");
  }

  // Display title of each markup samples as a select option
  function listMarkupAsOptions ($type) {
    $files = array();
    $handle=opendir('markup/'.$type);
    while (false !== ($file = readdir($handle))):
        if(stristr($file,'.html')):
            $files[] = $file;
        endif;
    endwhile;

    sort($files);
    foreach ($files as $file):
        $filename = preg_replace("/\.html$/i", "", $file); 
        $title = preg_replace("/\-/i", " ", $filename);
        $title = ucwords($title);
        echo '<option value="#sg-'.$filename.'">'.$title.'</option>';
    endforeach;
  }

  // Display markup view & source
  function showMarkup($type) {
    $files = array();
    $handle=opendir('markup/'.$type);
    while (false !== ($file = readdir($handle))):
        if(stristr($file,'.html')):
            $files[] = $file;
        endif;
    endwhile;

    sort($files);
    foreach ($files as $file):
        $filename = preg_replace("/\.html$/i", "", $file);
        $title = preg_replace("/\-/i", " ", $filename);
        $documentation = 'doc/'.$type.'/'.$file;
        echo '<div class="sg-markup sg-section">';
        echo '<div class="sg-display">';
        echo '<h2 class="sg-h2"><a id="sg-'.$filename.'" class="sg-anchor">'.$title.'</a></h2>';
        if (file_exists($documentation)) {
          echo '<div class="sg-doc">';
          echo '<h3 class="sg-h3">Usage</h3>';
          include($documentation);
          echo '</div>';
        }
        echo '<h3 class="sg-h3">Example</h3>';
        include('markup/'.$type.'/'.$file);
        echo '</div>';
        echo '<div class="sg-markup-controls"><a class="sg-btn sg-btn--source" href="#">View Source</a> <a class="sg-btn--top" href="#top">Back to Top</a> </div>';
        echo '<div class="sg-source sg-animated">';
        echo '<a class="sg-btn sg-btn--select" href="#">Copy Source</a>';
        echo '<pre class="prettyprint linenums"><code>';
        echo htmlspecialchars(file_get_contents('markup/'.$type.'/'.$file));
        echo '</code></pre>';
        echo '</div>';
        echo '</div>';
    endforeach;
  }
?>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
  <title>Spendensammler - Style Guide</title>
  <meta name="viewport" content="width=device-width">
  <!-- Style Guide Boilerplate Styles -->
  <link rel="stylesheet" href="css/sg-style.css">
  
  <!-- Replace below stylesheet with your own stylesheet -->
  <link href='http://fonts.googleapis.com/css?family=Open+Sans:400,700,400italic' rel='stylesheet' type='text/css'>
  <link rel="stylesheet" href="css/screen.css">
</head>
<body>
    
<div id="top" class="sg-header sg-container">
  <h1 class="sg-logo">SPENDENSAMMLER <span>STYLE GUIDE</span></h1>
  <form id="js-sg-nav" action=""  method="post" class="sg-nav">
    <select id="js-sg-section-switcher" class="sg-section-switcher" name="sg_section_switcher">
        <option value="">Jump To Section:</option>
        <optgroup label="Intro">
          <option value="#sg-about">About</option>
          <option value="#sg-colors">Colors</option>
          <option value="#sg-fontStacks">Font-Stacks</option>
        </optgroup>
        <optgroup label="Base Styles">
          <?php listMarkupAsOptions('base'); ?>
        </optgroup>
        <optgroup label="Pattern Styles">
          <?php listMarkupAsOptions('patterns'); ?>
        </optgroup>
    </select>
    <input type="hidden" name="sg_uri" value="<?php echo $_SERVER["SERVER_NAME"].$_SERVER["REQUEST_URI"]; ?>">
    <button type="submit" class="sg-submit-btn">Go</button>
  </form><!--/.sg-nav-->
</div><!--/.sg-header-->

<div class="sg-body sg-container">
  <div class="sg-info">               
    <div class="sg-about sg-section">
      <h2 class="sg-h2"><a id="sg-about" class="sg-anchor">About</a></h2>
      <p>
      	This document showcases the basic styles intended to use for the <b>Spendensammler</b> application
      	built during the "Web Application Engineering &amp; Content Management" course at the Technical University of Vienna.
      </p>
      <p>
      	The <a href="#general-styles">first part</a> covers basic styles and components that usually appear in any website.
      </p>
      <p>
      	The <a href="#specific-styles">second part</a> covers styles and components specifically used in this application.
      </p>
    </div><!--/.sg-about-->
    
    <a id="general-styles"></a>
    <div class="sg-colors sg-section">
      <h2 class="sg-h2"><a id="sg-colors" class="sg-anchor">Colors</a></h2>
      	
      	<h3>Blue (general color for layout styles)</h3>
        <div class="sg-color sg-color--dark-blue"><span class="sg-color-swatch"><span class="sg-animated">#03426a</span></span></div>
        <div class="sg-color sg-color--grey-blue"><span class="sg-color-swatch"><span class="sg-animated">#24587a</span></span></div>
        <div class="sg-color sg-color--blue"><span class="sg-color-swatch"><span class="sg-animated">#0a67a3</span></span></div>
        <div class="sg-color sg-color--light-blue"><span class="sg-color-swatch"><span class="sg-animated">#3e97d1</span></span></div>
        <div class="sg-color sg-color--very-light-blue"><span class="sg-color-swatch"><span class="sg-animated">#65a6d1</span></span></div>
        
        <h3>Grey (additional layout colors, disabled elements)</h3>
        <div class="sg-color sg-color--anthrazit"><span class="sg-color-swatch"><span class="sg-animated">#1a1a1a</span></span></div>
        <div class="sg-color sg-color--grey"><span class="sg-color-swatch"><span class="sg-animated">#696969</span></span></div>
        <div class="sg-color sg-color--semi-light-grey"><span class="sg-color-swatch"><span class="sg-animated">#cccccc</span></span></div>
        <div class="sg-color sg-color--light-grey"><span class="sg-color-swatch"><span class="sg-animated">#dddddd</span></span></div>
        <div class="sg-color sg-color--very-light-grey"><span class="sg-color-swatch"><span class="sg-animated">#eeeeee</span></span></div>
        
        <h3>Green (submit buttons, alert states)</h3>
        <div class="sg-color sg-color--light-green"><span class="sg-color-swatch"><span class="sg-animated">#36da82</span></span></div>
        <div class="sg-color sg-color--very-light-green"><span class="sg-color-swatch"><span class="sg-animated">#62da9a</span></span></div>
        
        <h3>Red (reset/cancel buttons, alert states)</h3>
        <div class="sg-color sg-color--light-red"><span class="sg-color-swatch"><span class="sg-animated">#ff6440</span></span></div>
        <div class="sg-color sg-color--very-light-red"><span class="sg-color-swatch"><span class="sg-animated">#ff8e73</span></span></div>
        
        <div class="sg-markup-controls"><a class="sg-btn--top" href="#top">Back to Top</a></div>
    </div><!--/.sg-colors-->
    
    <div class="sg-font-stacks sg-section">
      <h2 class="sg-h2"><a id="sg-fontStacks" class="sg-anchor">Font Stacks</a></h2>
      <p class="sg-font sg-font-primary">"Open Sans", Arial, sans-serif</p>
      <div class="sg-markup-controls"><a class="sg-btn--top" href="#top">Back to Top</a></div>
    </div><!--/.sg-font-stacks-->
  </div><!--/.sg-info-->    

  <div class="sg-base-styles">    
    <h1 class="sg-h1">Base Styles</h1>
    <?php showMarkup('base'); ?>
  </div><!--/.sg-base-styles-->
  
  <a id="specific-styles"></a>
  <div class="sg-pattern-styles">
    <h1 class="sg-h1">Pattern Styles</h1>
    <p>Will be added once the overall layout has been defined.</p>
    <?php // showMarkup('patterns'); ?>
    </div><!--/.sg-pattern-styles-->
  </div><!--/.sg-body-->

  <script src="js/sg-plugins.js"></script>
  <script src="js/sg-scripts.js"></script>
</body>
</html>
 