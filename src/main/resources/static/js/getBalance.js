GET: $(document).ready(
    function() {

        // GET REQUEST
        $("#revealBalance").click(function(event) {
            event.preventDefault();
            ajaxGet();
        });

        // DO GET
        function ajaxGet() {
            $.ajax({
                type : "GET",
                url : "/revealBalance",
                success : function(data) {
                    $('#fiatBalance').empty();
                    var p=document.createElement('p');
                    p.setAttribute("class", "text-muted mb-0");
                    p.innerHTML=new Intl.NumberFormat(`en-US`, {
                        currency: `USD`,
                        style: 'currency',
                    }).format(data);

                    $("#fiatBalance").append(p);
                    console.log("Success: ", data);
                },
                error : function(e) {
                    $("#getCryptosDiv").html("<strong>Error</strong>");
                    console.log("ERROR: ", e);
                }
            });
        }
    })