var nbMove = 0;
var oldNbMove = 0;
var oldPosX = "(0%";
var oldPosY = "0%)";

function addAbs(id) {
    $.ajax({
        url: urlAddAbs,       // url créée dans le fichier etudiants.jsp
        type: 'post',
        dataType: 'json',
        data: {id: id},   // Donnée envoyée au serveur

        success: function (data) {    // Donnée reçue du serveur
            let text = "Nombre d'absence";
            if (data >= 2) {
                text = text + "s"
            }
            document.getElementById("ABS-" + id).innerHTML = text + " : " + data;

            if (data !== 0) {
                document.getElementById("notifAbs-" + id).classList.value = "alert alert-danger";
            }
        },

        error: function (error) {
            console.log("Erreur de la requête ajax");
        }
    });
}


function deleteGrade(idGrade) {
    $.ajax({
        url: urlDeleteGrade,       // url créée dans le fichier etudiants.jsp
        type: 'post',
        dataType: 'json',
        data: {idGrade: idGrade},   // Donnée envoyée au serveur

        success: function (data) {    // Donnée reçue du serveur
            let divNote = document.getElementById("divNote-" + idGrade);
            let moyenne = document.getElementById("moyenne");
            divNote.remove();
            if (data === -1) {
                moyenne.type = "text";
                moyenne.value = "Non noté";
            } else {
                document.getElementById("moyenne").value = data;
            }
        },

        error: function (error) {
            console.log("Erreur de la requête ajax");
        }
    });
}

function deleteUser(idUser, typeEtu) {
    $.ajax({
        url: urlDeleteUser,
        type: 'post',
        dataType: 'json',
        data: {idUser: idUser, typeUser: typeEtu},   // Donnée envoyée au serveur

        success: function (data) {    // Donnée reçue du serveur
            if (data) {
                if (confirm("J'ai menti, ton compte vient d'être supprimé.")) {
                    document.getElementById("btnDisconnect").click();
                }
            } else {
                alert("Le développeur a décidé de te laisser une chance. Ton compte n'a pas été supprimé.")
            }

        },

        error: function (error) {
            alert("Le développeur a décidé de te laisser une chance. Ton compte n'a pas été supprimé.")
        }
    });
}

function changerGroupe() {
    if (document.getElementById("allClasses").checked) {
        document.getElementById("selectGroupes").style.visibility = "hidden";
    } else {
        document.getElementById("selectGroupes").style.visibility = "visible";
    }
}


function isAbsent(id) {
    let note = document.getElementById("note-" + id);
    if (note.required) {
        note.disabled = true;
        note.required = false;
        note.placeholder = "Absent";
    } else {
        note.disabled = false;
        note.required = true;
        note.placeholder = "Nouvelle note";
    }
}


function togglePasswordVisibility() {
    var passwordInput = document.getElementById("champ_2");
    var eye = document.getElementById("eye-img");
    if (passwordInput.type === "password") {
        passwordInput.type = "text";
        eye.classList.remove("fa-eye-slash")
        eye.classList.add("fa-eye");
    } else {
        passwordInput.type = "password";
        eye.classList.remove("fa-eye")
        eye.classList.add("fa-eye-slash");
    }
}

/**
 * type = 1 => comparer l'égalité de deux champs
 * type = 2 => vérifier si deux champs sont vides
 * @param type
 * @returns {boolean}
 */
function checkFields(type) {
    oldNbMove += 1;
    let champ_1 = document.getElementById("champ_1");
    let champ_2 = document.getElementById("champ_2");
    let btnValider = document.getElementById("btnValider");

    if (type === 1) {
        let typeUser = document.getElementById("typeUser").value;
        //if (typeUser === 1) {
            if (champ_1.value !== champ_2.value) {
                if (nbMove === 25 && oldNbMove === nbMove) {
                    alert("Il se pourrait que les mots de passe ne correspondent pas...");
                } else if (nbMove === 30 && oldNbMove === nbMove) {
                    alert("Essaye encore 10 fois et ton compte sera supprimé.");
                } else if (nbMove === 38 && oldNbMove === nbMove) {
                    alert("Il te reste encore deux chances...");
                } else if (nbMove === 39 && oldNbMove === nbMove) {
                    let idUser = document.getElementById("idUser").value;
                    deleteUser(idUser, typeUser);
                }
                if (oldNbMove !== nbMove) {
                    oldNbMove = nbMove;
                }
            


            btnValider.classList.remove("btn-primary");
            btnValider.classList.add("btn-danger");
            champ_2.setCustomValidity("Les mots de passe ne correspondent pas");
            return false;
        } else {
            btnValider.classList.remove("btn-danger");
            btnValider.classList.add("btn-primary");
            btnValider.style.transform = "translate(0%,0%)"
            champ_2.setCustomValidity("");
            return true;
        }
    } else if (type === 2) {
        if (champ_1.value === "" || champ_2.value === "") {
            btnValider.classList.remove("btn-primary");
            btnValider.classList.add("btn-danger");
            if (champ_1.value === "") {
                champ_1.setCustomValidity("Ce champ ne doit pas être vide");
            } else {
                champ_2.setCustomValidity("Ce champ ne doit pas être vide");
            }
            return false;
        } else {
            btnValider.classList.remove("btn-danger");
            btnValider.classList.add("btn-primary");
            btnValider.style.transform = "translate(0%,0%)";
            champ_1.setCustomValidity("");
            champ_2.setCustomValidity("");
            return true;
        }
    }
}

function moveBtn(type) {
    let btnValider = document.getElementById("btnValider");
    if (!checkFields(type)) {
        nbMove += 1;
        btnValider.type = "button";

        if (nbMove > 10) {
            let Yoffset;
            if (nbMove < 20) {
                //Random int entre 20 et 80.
                Yoffset = Math.floor(Math.random() * (80 - 20 + 1) + 20);
            } else {
                //Random int entre 50 et 500.
                Yoffset = Math.floor(Math.random() * ((nbMove * 25) - 50 + 1) + 50);
            }

            let signe = "";
            if (Yoffset >= 138 || Yoffset % 2 === 0) {
                signe = "-";
            }
            oldPosY = signe + Yoffset + "%)";
        } else {
            oldPosY = "0%)";
        }

        if (oldPosX === "(100%") {
            oldPosX = "(-100%";

        } else if (oldPosX === "(-100%") {
            oldPosX = "(100%";

        } else {
            oldPosX = "(100%";
        }
        btnValider.style.transform = "translate" + oldPosX + "," + oldPosY;
    } else {
        nbMove = 0;
        btnValider.type = "submit";
        btnValider.style.transform = "translate(0%,0%)";
    }
}

function changerAdmin() {
    if (document.getElementById("isAdmin").checked) {
        document.getElementById("selectGroupes").style.visibility = "hidden";
    } else {
        document.getElementById("selectGroupes").style.visibility = "visible";
    }
}
