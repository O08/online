  var  trackerPackage_page = new Vue({
       el:'#page',
        data: {
            showSearchWrap: false,
            trackingNo: '',
            trackers: []
         }
    });
 // call tracker api
 function trackShipment(shipmentId){
   var formData = {
    shipmentId: shipmentId
   }
   $.post('api/v1/selfService/trackShipment',formData,function(data) {
                var trackersResponse = JSON.parse(data.data.trackers);// response is json str
                 trackerPackage_page.trackers = trackersResponse.data;
                 trackerPackage_page.trackingNo = trackersResponse.nu;
//                trackerPackage_page.trackers = {};
//                trackerPackage_page.trackers.push(trackersResponseJson.data) ;
               })
                 .fail(function(data) {
                   // place error code here
                 });
 }
 initTackPackagePage();
 // init trackers
  function initTackPackagePage(){
    var shipmentId = getQueryVariable('shipmentId');
    trackShipment(shipmentId);
  }
    // get url param
  function getQueryVariable(variable){
    var query = window.location.search.substring(1);
           var vars = query.split("&");
           for (var i=0;i<vars.length;i++) {
                   var pair = vars[i].split("=");
                   if(pair[0] == variable){return pair[1];}
           }
           return(false);
  }
  function toggleSearch(){
     trackerPackage_page.showSearchWrap = !trackerPackage_page.showSearchWrap;
  }