(function() {
    tinymce.PluginManager.requireLangPack('acheck');
    tinymce.create('tinymce.plugins.AcheckPlugin', {
    	init: function(ed, url) {
            ed.addCommand('mceACheck', function() {
                var theCode = '<html><body onLoad="document.accessform.submit();"> \n';
                theCode += '<h1>Submitting Code for Accessibility Checking.....</h1>\n';
                theCode += '<form action="http://achecker.ca/checker/index.php" name="accessform" method="post"> \n';
                theCode += '<input type="hidden" name="gid[]" value="8" /> \n';
                theCode += '<input type="hidden" name="show_source" value="1" /> \n';
                theCode += '<textarea name="validate_content">' + tinyMCE.activeEditor.getContent({
                    format: 'raw'
                }) + '</textarea>\n';
                theCode += '<input type="submit" /></form> \n';
                theCode += '</body></html> \n';
                accessWin = window.open('', '_blank');
                accessWin.document.writeln(theCode);
                accessWin.document.close();
            });
            ed.addButton('acheck', {
                title: 'acheck.desc',
                cmd: 'mceACheck',
                image: url + '/img/acheck.gif'
            });
            ed.onNodeChange.add(function(ed, cm, n) {
                cm.setActive('acheck', n.nodeName == 'acheck');
            });
        },
        
        createControl: function(n, cm) {
            return null;
        },
        getInfo: function() {
            return {
                longname: 'ACheck Plugin',
                author: 'ATutor',
                authorurl: 'http://www.atutor.ca',
                infourl: 'http://www.atutor.ca',
                version: "1.0"
            };
        }
    });
    tinymce.PluginManager.add('acheck', tinymce.plugins.AcheckPlugin);
})();