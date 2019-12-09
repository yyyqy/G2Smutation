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
       var struct1;
       var pdb1 = getUrlVariable("pdb1");
       var pdbName1 = getPdbName(pdb1);
       var chain1 = getPdbChain(pdb1);
       var res1 = getPdbRes(pdb1);
       
       var pdburl1 = "rcsb://" + pdbName1;
       
       var chainColor1 = "lightgreen";
       var resColor1 = "tomato";
       
       Promise.all([
    	   stage.loadFile(pdburl1).then(function (o) {
    		 var sele11 = "polymer and :" + chain1;
    		 o.addRepresentation("cartoon", { sele: sele11, color: chainColor1 })
    	     o.autoView()
    	     return o
    	   })

    	 ]).then(function (ol) {
    	   var s1 = ol[ 0 ].structure
    	   var fchain1 = ':' + chain1
    	   var sele12 = res1 + ":" + chain1;
    	   ol[ 0 ].addRepresentation("ball+stick", {sele: sele12, color: resColor1, scale: 1.0});
    	   ol[ 0 ].updateRepresentations({ 'position': true })
    	   ol[ 0 ].autoView(sele12)
    	 })
    	 
    	 Promise.all;    
// alignment and highlight residue end
});