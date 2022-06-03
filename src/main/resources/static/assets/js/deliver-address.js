var deliverAddressPage = new Vue({
    el:'#page',
    data: {
      addressList: [],
      formShipping: {},
      currentActiveForm: 1 // 1: add from 2: edit form
    }
 });
 loadAddressList();
 // request customer address list
 function loadAddressList(){
   $.post('api/v1/selfService/address' ,function(data) {
                 deliverAddressPage.addressList = data.data;
             })
               .fail(function(data) {
                 // place error code here
               });
 }

function addAddress(){

   $.post('api/v1/selfService/newAddress' ,deliverAddressPage.formShipping,function(data) {

             })
               .fail(function(data) {
                 // place error code here
               });
}

function editAddress(){
 $.post('api/v1/selfService/modifyAddress' ,deliverAddressPage.formShipping, function(data) {
             $("#modal-modify-address").modal('hide');
           })
             .fail(function(data) {
               // place error code here
             });
}

function delAddress(id){


$.post('api/v1/selfService/removeAddress' ,{id: id},function(data) {

          })
            .fail(function(data) {
              // place error code here
            });
}
// edit address
// id : addressId
function goEditForm(id){
// edit : currentActiveForm = 2
  deliverAddressPage.currentActiveForm = 2;
  for(var i in deliverAddressPage.addressList){
    if(deliverAddressPage.addressList[i].id == id){
      deliverAddressPage.formShipping = deliverAddressPage.addressList[i];
      return;
    }
  }
}
