document.body.style.backgroundColor = "black";

document.addEventListener("DOMContentLoaded", function () {


// ngl stage components start
       var stage = new NGL.Stage("viewport");
         stage.loadFile("").then(function (component) {
         component.addRepresentation("cartoon");
         component.autoView();
         }
    );

       window.addEventListener( "resize", function( event ){
           stage.handleResize();
       }, false );


    function addElement (el) {
        Object.assign(el.style, {
            position: "absolute",
            zIndex: 100
        });
        stage.viewer.container.appendChild(el);
    }

       function createElement (name, properties, style) {
           var el = document.createElement(name);
           Object.assign(el, properties);
           Object.assign(el.style, style);
           return el;
       }
       
       function createElementWithId (name, properties, style) {
           var el = document.createElement(name);
           el.id = "align";
           Object.assign(el, properties);
           Object.assign(el.style, style);
           return el;
       }

       function createSelect (options, properties, style) {
           var select = createElement("select", properties, style);
           options.forEach(function (d) {
               select.add(createElement("option", {
                   value: d[ 0 ], text: d[ 1 ]
               }));
           });
           return select;
       }

       function createFileButton (label, properties, style) {
           var input = createElement("input", Object.assign({
               type: "file"
           }, properties), { display: "none" });
           addElement(input);
           var button = createElement("input", {
               value: label,
               type: "button",
               onclick: function () { input.click(); }
           }, style);
           return button;
       }
       
       function getZoomDistance() { 
    	    return stage.viewer.camera.position.clone()
    	           .sub( stage.viewer.controls.target )
    	           .length();
    	}
       
// ngl stage components end

// url info start
       function getUrlVariable(variable){
           var query = window.location.search.substring(1);
           var vars = query.split("&");
           for (var i=0;i<vars.length;i++) {
              var pair = vars[i].split("=");
              if(pair[0] == variable){return pair[1];}
           }
           return(false);
       }
       
       function getPdbName(variable){
    	   var name = variable.split("_")[0];
    	   return name;
       }
       
       function getPdbChain(variable){
    	   var chain = variable.split("_")[1];
    	   return chain;
       }
       
       function getPdbRes(variable){
    	   var res = variable.split("_")[2];
    	   return res;
       }
       
       var ensembl = getUrlVariable("ensembl");
       var position = getUrlVariable("position");
       document.getElementById("title2").innerHTML="Mapped PDB residue from " + ensembl + " on positon " + position;

// url info end    
// init dropdownlist data start
       var map = new Map();
       var resArray = new Array("R","Y","A");
       var PDBArray0 = new Array("2fej_A_202","2xwr_B_202","5mct_B_202","5mct_A_202");
       var PDBArray1 = new Array("2p52_A_199","2ioo_A_1199","2ioi_A_1199");
       var PDBArray2 = new Array("1hu8_C_199","1hu8_B_199","1hu8_A_199");
       
       map.set(resArray[0], PDBArray0);
       map.set(resArray[1], PDBArray1);
       map.set(resArray[2], PDBArray2);
       
// init dropdownlist data end 
// dropdown list design
       var selectPDB1Res = document.getElementById("selectPDB1Res"); 
       for(var i = 0; i < resArray.length; i++) {
           var res = resArray[i];
           var el = document.createElement("option");
           el.textContent = res;
           el.value = res;
           selectPDB1Res.appendChild(el);
       }
       
       var selectPDB2Res = document.getElementById("selectPDB2Res"); 
       for(var i = 0; i < resArray.length; i++) {
           var res = resArray[i];
           var el = document.createElement("option");
           el.textContent = res;
           el.value = res;
           selectPDB2Res.appendChild(el);
       }
       
       $(document).ready(function(){
    	    function ddlChangePDB1(){
    	    	var selectPDB1 = document.getElementById("selectPDB1"); 
    	    	selectPDB1.length = 0;
    	    	var index = selectPDB1Res.selectedIndex; 
    	    	var res1 = selectPDB1Res.options[index].value;
    	    	var PDBArray = map.get(res1);
    	    	for(var i = 0; i < PDBArray.length; i++) {
    	            var PDB = PDBArray[i];
    	            var el = document.createElement("option");
    	            el.textContent = PDB;
    	            el.value = PDB;
    	            selectPDB1.appendChild(el);
    	        }
    	    }
    	    $("#selectPDB1Res").on("change", ddlChangePDB1);
    	});
       
       $(document).ready(function(){
   	    	function ddlChangePDB2(){
   	    		var selectPDB2 = document.getElementById("selectPDB2"); 
   	    		selectPDB2.length = 0;
   	    		var index = selectPDB2Res.selectedIndex; 
   	    		var res2 = selectPDB2Res.options[index].value;
   	    		var PDBArray = map.get(res2);
   	    		for(var i = 0; i < PDBArray.length; i++) {
   	    			var PDB = PDBArray[i];
   	    			var el = document.createElement("option");
   	    			el.textContent = PDB;
   	    			el.value = PDB;
   	    			selectPDB2.appendChild(el);
   	    		}
   	    	}
   	    	$("#selectPDB2Res").on("change", ddlChangePDB2);
   		});
       
       
// alignment and highlight residue start
       var selectPDB1;
       var index1;
       var pdb1;
       var pdbName1;
       var chain1;
       var res1;
       
       var selectPDB2;
       var index2;
       var pdb2;
       var pdbName2;
       var chain2;
       var res2;

       var pdburl1;
       var pdburl2;    
       
       var chainColor1 = "lightgreen";
       var chainColor2 = "dodgerblue";
       var resColor1 = "tomato";
       var resColor2 = "yellow";
       
       function updateParameters(){
    	   selectPDB1 = document.getElementById("selectPDB1"); 
           index1 = selectPDB1.selectedIndex; 
           pdb1 = selectPDB1.options[index1].value;
           pdbName1 = getPdbName(pdb1);
           chain1 = getPdbChain(pdb1);
           res1 = getPdbRes(pdb1);

           selectPDB2 = document.getElementById("selectPDB2"); 
           index2 = selectPDB2.selectedIndex; 
           pdb2 = selectPDB2.options[index2].value;
           pdbName2 = getPdbName(pdb2);
           chain2 = getPdbChain(pdb2);
           res2 = getPdbRes(pdb2);

           pdburl1 = "rcsb://" + pdbName1;
           pdburl2 = "rcsb://" + pdbName2; 
       }
       
       function alignment(){
	       Promise.all([	    	   
	    	   stage.loadFile(pdburl1, {firstModelOnly:true}).then(function (o) {
	    		 var sele11 = "polymer and :" + chain1;
	    		 o.addRepresentation("cartoon", { sele: sele11, color: chainColor1})
	    	     o.autoView()
	    	     return o
	    	   }),
	
	    	   stage.loadFile(pdburl2, {firstModelOnly:true}).then(function (o) {
	    		 var sele21 = "polymer and :" + chain2;
	             o.addRepresentation("cartoon", { sele: sele21, color: chainColor2});
	    	     o.autoView()
	    	     return o
	    	   })
	
	    	 ]).then(function (ol) {
	    	   var s1 = ol[ 0 ].structure
	    	   var s2 = ol[ 1 ].structure
	    	   var fchain1 = ':' + chain1
	    	   var fchain2 = ':' + chain2
	    	   NGL.superpose(s1, s2, true, fchain1, fchain2)
	    	   ol[ 0 ].updateRepresentations({ 'position': true })
	    	   var sele12 = res1 + ":" + chain1;
	    	   ol[ 0 ].addRepresentation("ball+stick", {sele: sele12, color: resColor1, scale: 1.0});
	    	   var sele22 = res2 + ":" + chain2;
	    	   ol[ 1 ].addRepresentation("ball+stick", {sele: sele22, color: resColor2, scale: 1.0});
	    	   ol[ 0 ].updateRepresentations({ 'position': true })
	    	   ol[ 1 ].autoView(sele22)
	    	 })
       	}
       
       var pdblegend1;
       var pdblegend2;
       var residuelegend1;
       var residuelegend2;
       
       $(document).ready(function(){
    	   $("button").click(function(){
    		   	stage.removeAllComponents();
    		   	$("span").remove();
    		    updateParameters();
  	    		alignment();
  	    		loadLegend(pdblegend1, pdblegend2, residuelegend1, residuelegend2);
  	    		console.log("alignmengt")
    	   });
  		});   
// alignment and highlight residue end
       
// add legend start
       function loadLegend(pdblegend1, pdblegend2, residuelegend1, residuelegend2){
       	 pdblegend1 = createElement('span', {
    	   innerText: pdbName1 + ' chain ' + chain1
    	 }, { top: "35px", right: '90px', color: 'lightgreen' });
    	 addElement(pdblegend1);
    	 
    	 pdblegend2 = createElement('span', {
      	   innerText: pdbName2 + ' chain ' + chain2
      	 }, { top: "55px", right: '90px', color: 'dodgerblue' });
      	 addElement(pdblegend2); 
    	 
      	residuelegend1 = createElement('span', {
       	   innerText: ' residue ' + res1
       	 }, { top: "35px", right: '12px', color: 'tomato' });
       	 addElement(residuelegend1); 
       	 
       	residuelegend2 = createElement('span', {
        	   innerText: ' residue ' + res2
       	}, { top: "55px", right: '12px', color: 'yellow' });
        addElement(residuelegend2);        	
       }
// add legend end 
   
//      	 var pdblegend1 = createElement('span', {
//      	   innerText: 'lightgreen: ' + pdbName1 + ' chain ' + chain1
//      	 }, { top: "35px", right: '12px', color: 'lightgreen' });
//      	 addElement(pdblegend1);
//      	 
//      	 var pdblegend2 = createElement('span', {
//        	   innerText: 'dodgerblue: ' + pdbName2 + ' chain ' + chain2
//        	 }, { top: "55px", right: '12px', color: 'dodgerblue' });
//        	 addElement(pdblegend2); 
//      	 
//        	var residuelegend1 = createElement('span', {
//         	   innerText: 'tomato: ' + pdbName1 + ' chain ' + chain1 + ' residue ' + res1
//         	 }, { top: "75px", right: '12px', color: 'tomato' });
//         	 addElement(residuelegend1); 
//         	 
//         	var residuelegend2 = createElement('span', {
//          	   innerText: 'yellow: ' + pdbName2 + ' chain ' + chain2 + ' residue ' + res2
//         	}, { top: "95px", right: '12px', color: 'yellow' });
//          addElement(residuelegend2);    
   });

