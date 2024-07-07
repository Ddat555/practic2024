
function select(thisElement) {
        var selectedText = thisElement.innerText;
        var id = thisElement.getAttribute('data-value');
        document.getElementById('displayText').innerText = selectedText;
        document.getElementById('inputValue').value = id;
    }

    function addProduct() {
        var name = document.getElementById('nameProd').value.trim();
        var count = document.getElementById('countProd').value.trim();

        var label = document.getElementById('listProduct');
        var newContent = "Название: " + name + ' Количество: ' + count + "\n";
        label.innerText += newContent;

        var hiddenFieldName = document.getElementById('hiddenFieldName');
        var hiddenFieldCount = document.getElementById('hiddenFieldCount')
        hiddenFieldName.value += (name + ",");
        hiddenFieldCount.value += (count + "\n");
    }

    function removeProduct(element){
        var hiddenFieldDelete = document.getElementById('hiddenFieldDelete');
        hiddenFieldDelete.value += (element.value + ",");
        element.parentElement.remove();

    }

    function selectRole(role) {
            document.getElementById('selectedRole').innerText = role;
            document.getElementById('selectedRoleInput').value = role;

            if (role === 'Фирма-доставщик') {
                document.getElementById('deliveryField').style.display = 'block';
            } else {
                document.getElementById('deliveryField').style.display = 'none';
            }
        }