(function() {
    let btnModal = document.getElementById("btnModal");

    if (btnModal.value === "false") {
        btnModal.click();
        console.log(btnModal.value + " CHECK")
        let id = document.getElementById("idUser").value;
        setConnected(id);
    } else {
        document.getElementById("btnModal").remove();
        document.getElementById("welcomeModal").remove();
        console.log("Already shown");
    }
})();


function setConnected(id) {
    $.ajax({
        url: urlSetConnected,       // url créée dans le fichier etudiants.jsp
        type: 'post',
        dataType: 'json',
        data: {id: id},   // Donnée envoyée au serveur

        success: function (data) {
            console.log(data);
        },

        error: function (error) {
            console.log(error);
            console.log("Erreur de la requête ajax");
        }
    });
}
