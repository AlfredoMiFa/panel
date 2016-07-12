var check = false;
function dis(){check = true;}

        var DOM = 0, MS = 0, OP = 0, b = 0;
        function CheckBrowser(){
                if (b == 0){
                        if (window.opera) OP = 1;
                        if(document.getElementById) DOM = 1;
                        if(document.all && !OP) MS = 1;
                        b = 1;
                }
        }
        function filter (begriff){
                var suche = begriff.value.toLowerCase();
                var table = document.getElementById("filetable");
                var ele;
                for (var r = 1; r < table.rows.length; r++){
                        ele = table.rows[r].cells[1].innerHTML.replace(/<[^>]+>/g,"");
                        if (ele.toLowerCase().indexOf(suche)>=0 )
                                table.rows[r].style.display = '';
                        else table.rows[r].style.display = 'none';
                }
        }
        function AllFiles(){
                for(var x=0;x < document.FileList.elements.length;x++){
                        var y = document.FileList.elements[x];
                        var ytr = y.parentNode.parentNode;
                        var check = document.FileList.selall.checked;
                        if(y.name == 'selfile' && ytr.style.display != 'none'){
                                if (y.disabled != true){
                                        y.checked = check;
                                }
                        }
                }
        }
        function popUp(URL){
                fname = document.getElementsByName("myFile")[0].value;
                if (fname != "")
                        window.open(URL+"?first&uplMonitor="+encodeURIComponent(fname),"","width=400,height=150,resizable=yes,depend=yes")
        }
        