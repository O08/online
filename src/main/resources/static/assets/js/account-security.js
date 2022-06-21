//
var accountsecurity_page = new Vue({
    el:'#page',
    data: {
      showSearchWrap: false,
      showEditAccountNameForm: false,
      showEditAccountPasswordForm: false,
      showEditAccountPhoneForm: false,
      currentActiveForm: 0, // 0: no active 1: edit name form 2:edit password form 3: edit phone form
      customer: {},
      timer:''
    }
 });

// toggle()
function activeEditForm(target){
  accountsecurity_page.currentActiveForm = target;
}
// edit name debounce handle
var debounceEditAccountName = _.debounce(function() {
           doEditAccountName();
          }, 1000, {
              'leading': true,
              'trailing': false
          });
function doEditAccountName(){
  if(!accountsecurity_page.customer.name || accountsecurity_page.customer.name.length > 400){
               return;
              }
              var formdata = {
                 name: accountsecurity_page.customer.name
              }
              $.post('api/v1/auth/doEditName' ,formdata,function(data) {
                           // update username
                           accountsecurity_page.customer.username = accountsecurity_page.customer.name;
                           // reset edit name form
                           accountsecurity_page.customer.name = '';
                            activeEditForm(0);
                        })
                          .fail(function(data) {
                            // place error code here
                          });

}
// edit password debounce handle
var debounceEditAccountPassword = _.debounce(function() {
           doEditAccountPassword();
          }, 1000, {
              'leading': true,
              'trailing': false
          });
// edit password
function doEditAccountPassword(){

   var password = $("#password").val();
   var confirmedPassword = $("#confirmed-password").val();
  var checkPasswords = password == confirmedPassword

  if(!accountsecurity_page.customer.password || $.trim(password).length == 0){
               var note = document.querySelector('#password');
                note.setCustomValidity("field can't be empty!");
                note.reportValidity();
   return;
  }
  if(!checkPasswords){
                var note = document.querySelector('#confirmed-password');
                note.setCustomValidity("Passwords do not match, please retype");
                note.reportValidity();
                return;
  }

    var publicKeyInfo = getPublicKeyInfo();
  var formdata = {
     password: accountsecurity_page.customer.password,
     time: publicKeyInfo.time
  }

  var encrypt = new JSEncrypt();// plugin encrypt
  encrypt.setPublicKey(publicKeyInfo.publicKey);// set publicKey
  var encrypted = encrypt.encrypt(JSON.stringify(formdata));// encrypt  data

  $.post('api/v1/auth/doEditPassword' ,{ encrypted: encrypted },function(data) {
                                activeEditForm(0);
                                // reset password form
                                accountsecurity_page.customer.password = '';
                                accountsecurity_page.customer.confirmPassword = '';
            })
              .fail(function(data) {
                // place error code here
              });
}

// edit phone debounce handle
var debounceEditAccountPhone = _.debounce(function() {
           doEditAccountPhone();
          }, 1000, {
              'leading': true,
              'trailing': false
          });
// edit phone
function doEditAccountPhone(){

  if(!accountsecurity_page.customer.phone || $.trim(accountsecurity_page.customer.phone).length == 0){
           var note = document.querySelector('#edit-customer-phone');
                note.setCustomValidity("field can't be empty!");
                note.reportValidity();
           return;
  }
  var formdata = {
     phone: accountsecurity_page.customer.phone
  }
  $.post('api/v1/auth/doEditPhone' ,formdata,function(data) {
                                // update mobile
                                accountsecurity_page.customer.mobile = accountsecurity_page.customer.phone;
                                // reset edit phone form
                                accountsecurity_page.customer.phone = '';
                                 // close edit form
                                  activeEditForm(0);

            })
              .fail(function(data) {
                // place error code here
              });
}

// auth
function getPublicKeyInfo(){
    var publicKeyInfo = '';
    $.ajax({
			url : 'api/v1/auth/getPublicKey',
			type : 'POST',
			async: false,
			data : {},
			dataType : 'json',
			success : function(result) {
				publicKeyInfo = result.data;
			}
	});
	return publicKeyInfo;
}

function checkPasswords(){

     var password = $("#password").val();
     	var confirmedPassword = $("#confirmed-password").val();
         var note = document.querySelector('#confirmed-password');

          password != confirmedPassword ? note.setCustomValidity("Passwords do not match, please retype")
           : note.setCustomValidity("");
           note.reportValidity();
}
loadCustomerInfo();
// call customer info api
function loadCustomerInfo(){
  $.post('/api/v1/nav/customer',function(data) {
               var customer = data.data;
               if(!!customer.username){
                 accountsecurity_page.customer = customer;
               }
              })
                .fail(function(data) {
                  // place error code here
                });
}

function toggleSearch(){
     accountsecurity_page.showSearchWrap = !accountsecurity_page.showSearchWrap;
  }



