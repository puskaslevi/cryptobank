GET: $(document).ready(
    function() {

        // GET REQUEST
        $("#filterCryptos").click(function(event) {
            event.preventDefault();
            ajaxGet();
        });

        // DO GET
        function ajaxGet() {

            var nameSearch = $('#nameSearch').val();
            var symbolSearch = $('#symbolSearch').val();

            $.ajax({
                type : "GET",
                url : "getCryptos?symbol=" + symbolSearch + "&name=" + nameSearch ,
                success : function(result) {
                    $('#cryptoTable').empty();
                    $.each(result, function(i, crypto) {
                        var tr = document.createElement('tr');
                        var tdsymbol = document.createElement('td');
                        var tdname = document.createElement('td');
                        var tdvalue = document.createElement('td');
                        var tdButtons = document.createElement('td');
                        var divButton = document.createElement('div');
                        var tradeButton = document.createElement('a');
                        var transferButton = document.createElement('a');
                        var updateButton = document.createElement('a');
                        var deleteButton = document.createElement('a');

                        tdsymbol.innerHTML = crypto.symbol;
                        tdname.innerHTML = crypto.name;
                        tdvalue.innerHTML=new Intl.NumberFormat(`en-US`, {
                            currency: `USD`,
                            style: 'currency',
                        }).format(crypto.value);

                        var dropDownMenu = document.createElement("div");
                        dropDownMenu.setAttribute("class", "dropdown dropdown-menu drop");
                        dropDownMenu.setAttribute("aria-labelledby", "dropDownButton");
                        var buyLink = document.createElement('a');
                        var sellLink = document.createElement('a');

                        buyLink.setAttribute("class", "dropdown-item");
                        buyLink.setAttribute("href", "/cryptos/trade/" + crypto.id + "?trade=buy");
                        buyLink.innerHTML="Buy";
                        sellLink.setAttribute("class", "dropdown-item");
                        sellLink.setAttribute("href", "/cryptos/trade/" + crypto.id + "?trade=sell");
                        sellLink.innerHTML="Sell";

                        dropDownMenu.append(buyLink, sellLink);

                        tradeButton.setAttribute("href", "/cryptos/trade/" + crypto.id);
                        tradeButton.setAttribute("class", "btn btn-info");
                        tradeButton.setAttribute("id", "dropDownButton");
                        tradeButton.setAttribute("data-toggle", "dropdown");
                        tradeButton.innerHTML="Trade";


                        transferButton.setAttribute("href", "/cryptos/transfer"+crypto.id);
                        transferButton.setAttribute("class", "btn btn-info");
                        transferButton.innerHTML="Transfer";

                        updateButton.setAttribute("href", "/cryptos/edit/"+crypto.id);
                        updateButton.setAttribute("class", "btn btn-success");
                        updateButton.innerHTML="Update";
                        updateButton.hidden =!!!authorize;

                        deleteButton.setAttribute("href", "/cryptos/"+crypto.id);
                        deleteButton.setAttribute("class","btn btn-danger");
                        deleteButton.innerHTML="Delete";
                        deleteButton.hidden=!!!authorize;

                        divButton.append(tradeButton, dropDownMenu, transferButton, updateButton, deleteButton);
                        divButton.setAttribute("class", "p-2");
                        tdButtons.append(divButton);

                        tr.append(tdsymbol, tdname, tdvalue, tdButtons);

                        $('#cryptoTable').append(tr);
                    });
                        console.log("Success: ", result);
                },
                error : function(e) {
                    $("#filterCryptos").html("<strong>Error</strong>");
                    console.log("ERROR: ", e);
                }
            });
        }
    })