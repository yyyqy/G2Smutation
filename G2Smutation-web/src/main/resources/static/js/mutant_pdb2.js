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

// url info end    
       
// alignment and highlight residue start
       var pdb1 = getUrlVariable("pdb1");
       var pdbName1 = getPdbName(pdb1);
       var chain1 = getPdbChain(pdb1);
       var res1 = getPdbRes(pdb1);

       var pdb2 = getUrlVariable("pdb2");
       var pdbName2 = getPdbName(pdb2);
       var chain2 = getPdbChain(pdb2);
       var res2 = getPdbRes(pdb2);

       var pdburl1 = "rcsb://" + pdbName1;
       var pdburl2 = "rcsb://" + pdbName2;
       
       var chainColor1 = "lightgreen";
       var chainColor2 = "dodgerblue";
       var resColor1 = "tomato";
       var resColor2 = "yellow";
       
       
       Promise.all([
    	   stage.loadFile(pdburl1).then(function (o) {
    		 var sele11 = "polymer and :" + chain1;
    		 o.addRepresentation("cartoon", { sele: sele11, color: chainColor1 })
    	     o.autoView()
    	     return o
    	   }),

    	   stage.loadFile(pdburl2).then(function (o) {
    		 var sele21 = "polymer and :" + chain2;
             o.addRepresentation("cartoon", { sele: sele21, color: chainColor2 });
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
    	 
    	 //Promise.all;    
// alignment and highlight residue end
       
// add legend start
       	 var pdblegend1 = createElement('span', {
    	   innerText: pdbName1 + ' chain ' + chain1
    	 }, { top: "35px", right: '90px', color: 'lightgreen' });
    	 addElement(pdblegend1);
    	 
    	 var pdblegend2 = createElement('span', {
      	   innerText: pdbName2 + ' chain ' + chain2
      	 }, { top: "55px", right: '90px', color: 'dodgerblue' });
      	 addElement(pdblegend2); 
    	 
      	var residuelegend1 = createElement('span', {
       	   innerText: ' residue ' + res1
       	 }, { top: "35px", right: '12px', color: 'tomato' });
       	 addElement(residuelegend1); 
       	 
       	var residuelegend2 = createElement('span', {
        	   innerText: ' residue ' + res2
       	}, { top: "55px", right: '12px', color: 'yellow' });
        addElement(residuelegend2);        	

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
