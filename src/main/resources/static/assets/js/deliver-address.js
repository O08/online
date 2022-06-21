var deliverAddressPage = new Vue({
    el:'#page',
    data: {
      showSearchWrap: false,
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
var debounceAddAddress = _.debounce(function() {
           addAddress();
          }, 1000, {
              'leading': true,
              'trailing': false
          });
function addAddress(){

   if(!checkAddAddressForm()){
     return ;
   }

   $.post('api/v1/selfService/newAddress' ,deliverAddressPage.formShipping,function(data) {
                // reset
                deliverAddressPage.formShipping = {} ;
                // reload  address list
                loadAddressList();
             })
               .fail(function(data) {
                 // place error code here
               });
}

var debounceEditAddress = _.debounce(function() {
           editAddress();
          }, 1000, {
              'leading': true,
              'trailing': false
          });

function editAddress(){
  if(!checkEditAddressForm()){
  return ;
  }
 $.post('api/v1/selfService/modifyAddress' ,deliverAddressPage.formShipping, function(data) {
             // reset
             $("#modal-modify-address").modal('hide');
             deliverAddressPage.formShipping = {} ;
             // display new address form
             deliverAddressPage.currentActiveForm= 1;

             // reload  address list
             loadAddressList();
           })
             .fail(function(data) {
               // place error code here
             });
}

function delAddress(id){


$.post('api/v1/selfService/removeAddress' ,{id: id},function(data) {
             // reload  address list
             loadAddressList();
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

function toggleSearch(){
     deliverAddressPage.showSearchWrap = !deliverAddressPage.showSearchWrap;
  }

function checkEditAddressForm(){

    if(!deliverAddressPage.formShipping.country || $.trim(deliverAddressPage.formShipping.country).length == 0){
                var note = document.querySelector('#edit-country');
                note.setCustomValidity("field can't be empty!");
                note.reportValidity();
                return false;
    }
    if(!deliverAddressPage.formShipping.postalCode || $.trim(deliverAddressPage.formShipping.postalCode).length == 0){
                var note = document.querySelector('#edit-postcode');
                note.setCustomValidity("field can't be empty!");
                note.reportValidity();
                return false;
    }
    if(!deliverAddressPage.formShipping.mobile || $.trim(deliverAddressPage.formShipping.mobile).length == 0){
                    var note = document.querySelector('#edit-mobile');
                    note.setCustomValidity("field can't be empty!");
                    note.reportValidity();
                    return false;
     }
    if(!deliverAddressPage.formShipping.receiver || $.trim(deliverAddressPage.formShipping.receiver).length == 0){
                    var note = document.querySelector('#edit-name');
                    note.setCustomValidity("field can't be empty!");
                    note.reportValidity();
                    return false;
   }

     return true;

}

function checkAddAddressForm(){

    if(!deliverAddressPage.formShipping.country || $.trim(deliverAddressPage.formShipping.country).length == 0){
                var note = document.querySelector('#add-country');
                note.setCustomValidity("field can't be empty!");
                note.reportValidity();
                return false;
    }
    if(!deliverAddressPage.formShipping.postalCode || $.trim(deliverAddressPage.formShipping.postalCode).length == 0){
                var note = document.querySelector('#add-postcode');
                note.setCustomValidity("field can't be empty!");
                note.reportValidity();
                return false;
        }
    if(!deliverAddressPage.formShipping.mobile || $.trim(deliverAddressPage.formShipping.mobile).length == 0){
                    var note = document.querySelector('#add-mobile');
                    note.setCustomValidity("field can't be empty!");
                    note.reportValidity();
                    return false;
     }
    if(!deliverAddressPage.formShipping.receiver || $.trim(deliverAddressPage.formShipping.receiver).length == 0){
                    var note = document.querySelector('#add-name');
                    note.setCustomValidity("field can't be empty!");
                    note.reportValidity();
                    return false;
    }

     return true;

}