const tabviewData = document.querySelector(".tabview-data").dataset;
const { filesLabel, modulesLabel, foldersLabel, languagesLabel } = tabviewData;
const renderModules = tabviewData.renderModules === "true";
const renderFolders = tabviewData.renderFolders === "true";
const renderLanguages = tabviewData.renderLanguages === "true";

var myTabs = new YAHOO.widget.TabView("statistics");

YAHOO.plugin.Dispatcher.delegate (new YAHOO.widget.Tab({
    label: filesLabel,
    dataSrc: 'files',
    cacheData: true,
    active: false
}), myTabs);

if (renderModules) {
    YAHOO.plugin.Dispatcher.delegate (new YAHOO.widget.Tab({
        label: modulesLabel,
        dataSrc: 'modules',
        cacheData: true,
        active: false
    }), myTabs);
}

if (renderFolders) {
    YAHOO.plugin.Dispatcher.delegate (new YAHOO.widget.Tab({
        label: foldersLabel,
        dataSrc: 'folders',
        cacheData: true,
        active: false
    }), myTabs);
}

if (renderLanguages) {
    YAHOO.plugin.Dispatcher.delegate (new YAHOO.widget.Tab({
        label: languagesLabel,
        dataSrc: 'languages',
        cacheData: true,
        active: false
    }), myTabs);
}

myTabs.set('activeIndex', 0);
