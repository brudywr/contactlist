$(document).ready(function () {

    var url = window.location;
    var arguments = extractArguments(this);
    populateContacts(arguments.search, arguments.pageNumber);

    $("#addBtn").click(function (event) {
        $('#viewUser').modal();
        $('#viewUser').modal('open');
    });

    $("#addModalBtn").click(function (event) {
        event.preventDefault();

        $.ajax({
            type: "POST",
            url: "/contact/add",
            data: $('#addContactForm').serialize(),
            success: function (data) {
                if (data.status === "OK") {
                    console.log("successfully added a new contact");
                    clearContacts();
                    populateContacts();
                    $('#viewUser').modal('hide');
                } else {
                    console.log("backend error occurred while adding a new contact");
                    $('#modalMessage').text = data.status;
                }
            },
            error: function (e) {
                console.log("error occurred while adding a new contact");
                $('#viewUser').modal('hide');
            }
        });
    });

    $("#pageNext").click(function (event) {
        var search = $('#search').val();
        $("#pageNext").attr("href", this.href + search);
    });

    $("#pageFirst").click(function (event) {
        var search = $('#search').val();
        $("#pageFirst").attr("href", this.href + search);
    });
    $("#pagePrev").click(function (event) {
        var search = $('#search').val();
        $("#pagePrev").attr("href", this.href + search);
    });
    $("#pageLast").click(function (event) {
        var search = $('#search').val();
        $("#pageLast").attr("href", this.href + search);
    });

    $("#searchBtn").click(function (event) {
        var search = $('#search').val();
        populateContacts(search, 1);
        $("#searchBtn").attr("href", this.href + search);
    });

    function extractArguments(element) {
        var search = $('#search').val();
        var pageStart = element.baseURI.indexOf('page=');
        var searchStart = element.baseURI.indexOf('search=');
        var pageNumber = 1;
        if (pageStart < 0) {
            pageNumber = 1
        } else {
            if (searchStart < 0) {
                pageNumber = element.baseURI.substring(pageStart + 5, element.baseURI.length - 1);
            } else {
                pageNumber = element.baseURI.substring(pageStart + 5, searchStart - 1);
            }
        }
        return {pageNumber: pageNumber, search: search};
    }

    function clearContacts() {
        $("#contactTable").find("tbody").empty();
    }

    function populateContacts(search, page) {
        var searchPart = "";
        if (typeof(search) != "undefined") {
            searchPart = search;
        }
        var pagePart = "";
        if (typeof(page) != "undefined") {
            pagePart = page;
        }
        $.ajax({
            type: "GET",
            url: url.origin + "/contacts",
            data: {"search": searchPart, "page": pagePart},
            success: function (data) {
                fillData(data);
            },
            error: function (e) {
                handleError(e);
            }
        });
    }

    function fillData(data) {
        $.each(data.content, function (i, contact) {
            var contactRow = '<tr>' +
                '<td>' + contact.name + '</td>' +
                '<td><img src="' + contact.url.trim() + '"/></td>' +
                '</tr>';

            $('#contactTable tbody').append(contactRow);
        });

        $("#contactTable tbody tr:odd").addClass("info");
        $("#contactTable tbody tr:even").addClass("success");
    }

    function handleError(e) {
        alert("ERROR: ", e);
        console.log("ERROR: ", e);
    }
});