//
var accountsecurity_page = new Vue({
    el:'#page',
    data: {
      showEditAccountNameForm: false,
      showEditAccountPasswordForm: false,
      showEditAccountPhoneForm: false,
      currentActiveForm: 0, // 0: no active 1: edit name form 2:edit password form 3: edit phone form
      customer: {}
    }
 });

// toggle()
function activeEditForm(target){
  accountsecurity_page.currentActiveForm = target;
}
// edit name
function doEditAccountName(){

  if(!accountsecurity_page.customer.name){
   return;
  }
  var formdata = {
     name: accountsecurity_page.customer.name
  }
  $.post('api/v1/auth/doEditName' ,formdata,function(data) {
                activeEditForm(0);
            })
              .fail(function(data) {
                // place error code here
              });
}

// edit password
function doEditAccountPassword(){

  if(!accountsecurity_page.customer.password){
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

// edit phone
function doEditAccountPhone(){

  if(!accountsecurity_page.customer.phone){
   return;
  }
  var formdata = {
     phone: accountsecurity_page.customer.phone
  }
  $.post('api/v1/auth/doEditPhone' ,formdata,function(data) {
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

     if(password != confirmedPassword){
       $("#password").setCustomValidity("Passwords do not match, please retype");
     }
}