

var viewer = new Cesium.Viewer('cesiumContainer');
var scene = viewer.scene;


var planeArray = [];
var locations = [];
var always;
function add5000Billboards() {
    clearInterval(always);
    var shapeUrl = '../Apps/Sandcastle/images/whiteShapes.png';
    var longitude = -75;
    var latitude = 40;
    var imgSubReg= new Cesium.BoundingRectangle(67, 80, 14, 14);
    var imgColors =  [Cesium.Color.LIME,Cesium.Color.BLUE,Cesium.Color.YELLOW,Cesium.Color.RED];
    var billboards = scene.primitives.add(new Cesium.BillboardCollection());
    for(var i=0; i<100;i++){
        latitude = 40;
        for(var k=0; k<50;k++){
            locations[i*50+k] = [longitude,latitude];
            planeArray[i*50+k] = billboards.add({
                               image : shapeUrl,
                               imageSubRegion : imgSubReg,
                               position : Cesium.Cartesian3.fromDegrees(longitude, latitude),
                               color : imgColors[(i*50+k)%4]
                           });
           latitude+=0.004;
        }
        longitude-=0.004;
    }
  always = setInterval(changeLocation,500);
}

function changeLocation(){
    var random1,random2,longitude,latitude;
    for(var i=0;i<planeArray.length;i++){
        random1 = Math.random()*0.02 + 0.99;
        random2 = Math.random()*0.02 + 0.99;
        longitude = locations[i][0]*random1;
        latitude = locations[i][1]*random2;
        planeArray[i].position = Cesium.Cartesian3.fromDegrees(longitude, latitude);
    }

}

function stop(){
clearInterval(always);console.log("stop interval");
}



