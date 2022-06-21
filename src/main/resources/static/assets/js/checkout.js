// checkout page obj
var checkout_page = new Vue({
    el:'#checkout-page-content',
    data: {
      addressList: [],
      formShipping: {
         addressLine1: '',
         addressLine2: '',
         city: '',
         state: '',
         country: '',
         postalCode: '',
         mobile: '',
         receiver: '',
         id: -1
      },
      orderSummary: {
         subtotal: 0.00,
         tax: 0.00,
         shipping: 0.00,
         total: 0.00
      },
      showSavedAddress: true,
      formShippingAdd: {},
      formShippingEdit: {},
    },
    created(){
       checkout_page_onload();
    }
 });

// add address debounce handle
var debounceAddAddress= _.debounce(function() {
           addAddress();
          }, 1000, {
              'leading': true,
              'trailing': false
          });

// edit address debounce handle
var debounceEditAddress = _.debounce(function() {
           editAddress();
          }, 1000, {
              'leading': true,
              'trailing': false
          });

// delete address debounce handle
var debounceDelAddress = _.debounce(function() {
           delAddress();
          }, 1000, {
              'leading': true,
              'trailing': false
          });
// pay order debounce handle
var debounceToPay = _.debounce(function() {
           toPay();
          }, 1000, {
              'leading': true,
              'trailing': false
          });

 function getDiscountPrice(item){
     return (item.price*(100-item.discountPercent)/100).toFixed(2);
 }
 function getItemTotal(item){
     return (item.quantity*item.price*(100-item.discountPercent)/100).toFixed(2);
 }
 // load address list
 function loadAddresses(){
        $.post('/api/v1/selfService/address' ,function(data) {
           checkout_page.addressList = data.data;
           // address record is empty ,display none
           if($.isEmptyObject(data.data)){
             checkout_page.showSavedAddress  = false;
             checkout_page.addressList = [];
             checkout_page.formShipping = {};
             return;
           }
           checkout_page.showSavedAddress  = true;
           // default show first record
           showAddress(checkout_page.addressList[0]);
           checkout_page.formShipping.selectAddress = 0 ;
         })
           .fail(function(data) {
             // place error code here
           });
 }
 // refresh address
 function addressSelectOnchange(){
        var address = checkout_page.addressList[checkout_page.formShipping.selectAddress];
         showAddress(address);
 }
 // bind shipping address
 function showAddress(address){
             checkout_page.formShipping.addressLine1 = address.addressLine1 ;
             checkout_page.formShipping.addressLine2 = address.addressLine2 ;
             checkout_page.formShipping.city         = address.city ;
             checkout_page.formShipping.postalCode   = address.postalCode ;
             checkout_page.formShipping.state        = address.state ;
             checkout_page.formShipping.country      = address.country ;
             checkout_page.formShipping.mobile       = address.mobile;
             checkout_page.formShipping.id       = address.id;
             checkout_page.formShipping.receiver = address.receiver;
 }

 // load order summary
 function showOrderSummary(){
       $.post('api/v1/order/orderSummary' ,function(data) {
              checkout_page.orderSummary.items = data.data.items;
              checkout_page.orderSummary.subtotal = data.data.subtotal;
              checkout_page.orderSummary.tax = data.data.tax;
              checkout_page.orderSummary.shipping = data.data.shipping;
              checkout_page.orderSummary.total = data.data.total;
          })
            .fail(function(data) {
              // place error code here
            });
 }


function checkout_page_onload(){
           loadAddresses();
           showOrderSummary();
}

function addAddress(){
var validForm = $('#add-address-form')[0].checkValidity();
 if(!validForm){
  return;
 }
   $.post('api/v1/selfService/newAddress' ,checkout_page.formShippingAdd,function(data) {
                  // reload address drop
                    loadAddresses();
                    // reset add form
                    checkout_page.formShippingAdd = {};
                 $("#modal-add-address").modal('hide');

             })
               .fail(function(data) {
                 // place error code here
               });
}
function editAddress(){
 var validForm = $('#edit-address-form')[0].checkValidity();
 if(!validForm){
  return;
 }
 $.post('api/v1/selfService/modifyAddress' ,checkout_page.formShippingEdit, function(data) {
             // reload address drop
               loadAddresses();
             $("#modal-modify-address").modal('hide');

           })
             .fail(function(data) {
               // place error code here
             });
}

function delAddress(){
var address = checkout_page.addressList[checkout_page.formShipping.selectAddress];

$.post('api/v1/selfService/removeAddress' ,{id: address.id},function(data) {
          // reload address drop
          loadAddresses();
           $("#modal-modify-address").modal('hide');
          })
            .fail(function(data) {
              // place error code here
            });
}

function toPay(){
// new order
var address = checkout_page.addressList[checkout_page.formShipping.selectAddress];
if(!address || !address.id){
  alert("Your Address is Not Selected yet!")
  return;
}
$.post('api/v1/order/newOrder' ,{addressId: address.id},function(data) {
          if(!!data.data){
              // go pay page
              var orderDetailsId = data.data;
              window.location.href= 'payPalV2/toPay.html?orderDetailsId=' + orderDetailsId;
          }
})
            .fail(function(data) {
              // place error code here
              alert('Wooho,Something wrong!')
            });

}


   var current_fs, next_fs, previous_fs; //fieldsets
    var opacity;
function progressbarNext(e){
 current_fs = $(e.target).parent();
     next_fs = $(e.target).parent().next();

     //Add Class Active
     $("#progressbar li").eq($("fieldset").index(next_fs)).addClass("active");

     //show the next fieldset
     next_fs.show();
     //hide the current fieldset with style
     current_fs.animate({opacity: 0}, {
         step: function(now) {
             // for making fielset appear animation
             opacity = 1 - now;

             current_fs.css({
                 'display': 'none',
                 'position': 'relative'
             });
             next_fs.css({'opacity': opacity});
         },
         duration: 600
     });
}
function progressbarPrevious(e){
    current_fs = $(e.target).parent();
    previous_fs = $(e.target).parent().prev();

    //Remove class active
    $("#progressbar li").eq($("fieldset").index(current_fs)).removeClass("active");

    //show the previous fieldset
    previous_fs.show();

    //hide the current fieldset with style
    current_fs.animate({opacity: 0}, {
        step: function(now) {
            // for making fielset appear animation
            opacity = 1 - now;

            current_fs.css({
                'display': 'none',
                'position': 'relative'
            });
            previous_fs.css({'opacity': opacity});
        },
        duration: 600
    });
}
function resetAddModal(){
// reset shipping add form
 checkout_page.formShippingAdd = {};
}
// fill section-modify-address
function fillEditForm(){
checkout_page.formShippingEdit = checkout_page.formShipping;
}

function rounding(number){
       return !number ? '' : number.toFixed(2) ;
}