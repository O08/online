$(document).ready(function(){(function($) {
     $('.product-list-add-button').on( 'click', function() {
             $('.section-product-list').toggle();
             $('.section-interaction').toggle();
             $('.interaction-out-spacer').toggle();
             $('.section-product-add').toggle();
         });

     $('.product-category-list-add-button').on( 'click', function() {
                  $('.section-category-list').toggle();
                  $('.section-interaction').toggle();
                  $('.interaction-out-spacer').toggle();
                  $('.section-product-category-add').addClass('active');
              });
     $('.discount-list-add-button').on( 'click', function() {
                  $('.section-discount-list').hide();
                  $('.section-interaction').toggle();
                  $('.interaction-out-spacer').toggle();
                  $('.section-discount-add').addClass('active');
              });
//     $('.product-gallery-thumbs__item-plus').on( 'click', function() {
//                   $('#product-image-file-0a').click();
//             });

     $('#js-product-nav .product-gallery-thumbs__item').on( 'click', function() {
             var src = $(this).attr('data-large-src');
             $('#js-product-nav .product-gallery-thumbs__item').removeClass('active');
             $(this).addClass('active');
             $('#js-product-big img').hide().attr('src', src).fadeIn(200);
         });
     $("#product_code").on( 'blur', function() {
             checkProductCode();
          });
     $(".btn.btn-secondary.cancel").on( 'click', function() {
                  refresh();
               });
     $("#search-product").on( 'click', function() {
                       getProductByName();
                    });

})(jQuery);
});