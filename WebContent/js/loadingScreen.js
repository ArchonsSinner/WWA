/**
 * JavaScript für den Ladebildschirm
 * 
 */

//diese Funktion wird dem Analyse starten Button zugeordnet
function showLoadingScreen(){
	document.getElementById("loadingScreen").innerHTML="Analyse wird durchgeführt...<br>Bitte Warten....";
	//"loadingScreen" ist ein Paragraph unter der Clusteranzahlauswahl und dem StartButton 
	return true;
	
}