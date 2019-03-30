/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
'use strict';
var flowableModeler = angular.module('flowableModeler', [
    'ngCookies',
    'ngResource',
    'ngSanitize',
    'ngRoute',
    'ngDragDrop',
    'mgcrea.ngStrap',
    'mgcrea.ngStrap.helpers.dimensions', // Needed for tooltips
    'ui.grid',
    'ui.grid.edit',
    'ui.grid.selection',
    'ui.grid.autoResize',
    'ui.grid.moveColumns',
    'ui.grid.cellNav',
    'ngAnimate',
    'pascalprecht.translate',
    'ngFileUpload',
    'angularSpectrumColorpicker',
    'duScroll',
    'dndLists',
    'ngHandsontable'
]);

flowableModeler.factory("editorManager", ["$http", function ($http) {
    var editorManager = Class.create({
        initialize: function () {
            this.treeFilteredElements = ["SubProcess", "CollapsedSubProcess"];
            this.canvasTracker = new Hash();
            this.structualIcons = {
                "SubProcess": "expanded.subprocess.png",
                "CollapsedSubProcess": "subprocess.png",
                "EventSubProcess": "event.subprocess.png"
            };

            this.current = this.modelId;
            this.loading = true;
        },
        getModelId: function () {
            return this.modelId;
        },
        setModelId: function (modelId) {
            this.modelId = modelId;
        },
        getCurrentModelId: function () {
            return this.current;
        },
        setStencilData: function (stencilData) {
            //we don't want a references!
            this.stencilData = jQuery.extend(true, {}, stencilData);
        },
        getStencilData: function () {
            return this.stencilData;
        },
        getSelection: function () {
            return this.editor.selection;
        },
        getSubSelection: function () {
            return this.editor._subSelection;
        },
        handleEvents: function (events) {
            this.editor.handleEvents(events);
        },
        setSelection: function (selection) {
            this.editor.setSelection(selection);
        },
        registerOnEvent: function (event, callback) {
            this.editor.registerOnEvent(event, callback);
        },
        getChildShapeByResourceId: function (resourceId) {
            return this.editor.getCanvas().getChildShapeByResourceId(resourceId);
        },
        getJSON: function () {
            return this.editor.getJSON();
        },
        getStencilSets: function () {
            return this.editor.getStencilSets();
        },
        getEditor: function () {
            return this.editor; //TODO: find out if we can avoid exposing the editor object to angular.
        },
        executeCommands: function (commands) {
            this.editor.executeCommands(commands);
        },
        getCanvas: function () {
            return this.editor.getCanvas();
        },
        getRules: function () {
            return this.editor.getRules();
        },
        eventCoordinates: function (coordinates) {
            return this.editor.eventCoordinates(coordinates);
        },
        eventCoordinatesXY: function (x, y) {
            return this.editor.eventCoordinatesXY(x, y);
        },
        updateSelection: function () {
            this.editor.updateSelection();
        },
        /**
         * @returns the modeldata as received from the server. This does not represent the current editor data.
         */
        getBaseModelData: function () {
            return this.modelData;
        },
        edit: function (resourceId) {
            //Save the current canvas in the canvastracker if it is the root process.
            this.syncCanvasTracker();

            this.loading = true;

            var shapes = this.getCanvas().getChildren();
            shapes.each(function (shape) {
                this.editor.deleteShape(shape);
            }.bind(this));

            shapes = this.canvasTracker.get(resourceId);
            if (!shapes) {
                shapes = JSON.stringify([]);
            }

            this.editor.loadSerialized({
                childShapes: shapes
            });

            this.getCanvas().update();

            this.current = resourceId;

            this.loading = false;
            FLOWABLE.eventBus.dispatch("EDITORMANAGER-EDIT-ACTION", {});
            FLOWABLE.eventBus.dispatch(FLOWABLE.eventBus.EVENT_TYPE_UNDO_REDO_RESET, {});
        },
        getTree: function () {
            //build a tree of all subprocesses and there children.
            var result = new Hash();
            var parent = this.getModel();
            result.set("name", parent.properties["name"] || "No name provided");
            result.set("id", this.modelId);
            result.set("type", "root");
            result.set("current", this.current === this.modelId)
            var childShapes = parent.childShapes;
            var children = this._buildTreeChildren(childShapes);
            result.set("children", children);
            return result.toObject();
        },
        _buildTreeChildren: function (childShapes) {
            var children = [];
            for (var i = 0; i < childShapes.length; i++) {
                var childShape = childShapes[i];
                var stencilId = childShape.stencil.id;
                //we are currently only interested in the expanded subprocess and collapsed processes
                if (stencilId && this.treeFilteredElements.indexOf(stencilId) > -1) {
                    var child = new Hash();
                    child.set("name", childShape.properties.name || "No name provided");
                    child.set("id", childShape.resourceId);
                    child.set("type", stencilId);
                    child.set("current", childShape.resourceId === this.current);

                    //check if childshapes

                    if (stencilId === "CollapsedSubProcess") {
                        //the save function stores the real object as a childshape
                        //it is possible that there is no child element because the user did not open the collapsed subprocess.
                        if (childShape.childShapes.length === 0) {
                            child.set("children", []);
                        } else {
                            child.set("children", this._buildTreeChildren(childShape.childShapes));
                        }
                        child.set("editable", true);
                    } else {
                        child.set("children", this._buildTreeChildren(childShape.childShapes));
                        child.set("editable", false);
                    }
                    child.set("icon", this.structualIcons[stencilId]);
                    children.push(child.toObject());
                }
            }
            return children;
        },
        syncCanvasTracker: function () {
            var shapes = this.getCanvas().getChildren();
            var jsonShapes = [];
            shapes.each(function (shape) {
                //toJson is an summary object but its not a json string.!!!!!
                jsonShapes.push(shape.toJSON());
            });
            this.canvasTracker.set(this.current, JSON.stringify(jsonShapes));
        },
        getModel: function () {
            this.syncCanvasTracker();

            //this is an object.
            var editorConfig = this.editor.getJSON();
            var model = {
                modelId: this.modelId,
                bounds: editorConfig.bounds,
                properties: editorConfig.properties,
                childShapes: JSON.parse(this.canvasTracker.get(this.modelId)),
                stencil: {
                    id: "BPMNDiagram",
                },
                stencilset: {
                    namespace: "http://b3mn.org/stencilset/bpmn2.0#",
                    url: "../editor/stencilsets/bpmn2.0/bpmn2.0.json"
                }
            };

            this._mergeCanvasToChild(model);

            return model;
        },
        bootEditor: function (response) {
            var data = response.data;
            //TODO: populate the canvas with correct json sections.
            //resetting the state
            this.canvasTracker = new Hash();

            var config = jQuery.extend(true, {}, data); //avoid a reference to the original object.
            if (!config.model.childShapes) {
                config.model.childShapes = [];
            }

            this.findAndRegisterCanvas(config.model.childShapes); //this will remove any childshapes of a collapseable subprocess.
            this.canvasTracker.set(config.modelId, JSON.stringify(config.model.childShapes)); //this will be overwritten almost instantly.

            this.modelData = data;

            this.editor = new ORYX.Editor(config);
            this.current = this.editor.id;
            this.loading = false;

            FLOWABLE.eventBus.editor = this.editor;
            FLOWABLE.eventBus.dispatch("ORYX-EDITOR-LOADED", {});
            FLOWABLE.eventBus.dispatch(FLOWABLE.eventBus.EVENT_TYPE_EDITOR_BOOTED, {});
        },
        findAndRegisterCanvas: function (childShapes) {
            for (var i = 0; i < childShapes.length; i++) {
                var childShape = childShapes[i];
                if (childShape.stencil.id === "CollapsedSubProcess") {
                    if (childShape.childShapes.length > 0) {
                        //the canvastracker will auto correct itself with a new canvasmodel see this.edit()...
                        this.findAndRegisterCanvas(childShape.childShapes);
                        //a canvas can't be nested as a child because the editor would crash on redundant information.
                        this.canvasTracker.set(childShape.resourceId, JSON.stringify(childShape.childShapes));
                        //reference to config will clear the value.
                        childShape.childShapes = [];
                    } else {
                        this.canvasTracker.set(childShape.resourceId, '[]');
                    }
                }
            }
        },
        _mergeCanvasToChild: function (parent) {
            for (var i = 0; i < parent.childShapes.length; i++) {
                var childShape = parent.childShapes[i]
                if (childShape.stencil.id === "CollapsedSubProcess") {

                    var elements = this.canvasTracker.get(childShape.resourceId);
                    if (elements) {
                        elements = JSON.parse(elements);
                    } else {
                        elements = [];
                    }
                    childShape.childShapes = elements;
                    this._mergeCanvasToChild(childShape);
                } else if (childShape.stencil.id === "SubProcess") {
                    this._mergeCanvasToChild(childShape);
                } else {
                    //do nothing?
                }
            }
        },
        dispatchOryxEvent: function (event) {
            FLOWABLE.eventBus.dispatchOryxEvent(event);
        },
        isLoading: function () {
            return this.loading;
        },
        navigateTo: function (resourceId) {
            //TODO: this could be improved by check if the resourceId is not equal to the current tracker...
            this.syncCanvasTracker();
            var found = false;
            this.canvasTracker.each(function (pair) {
                var key = pair.key;
                var children = JSON.parse(pair.value);
                var targetable = this._findTarget(children, resourceId);
                if (!found && targetable) {
                    this.edit(key);
                    var flowableShape = this.getCanvas().getChildShapeByResourceId(targetable);
                    this.setSelection([flowableShape], [], true);
                    found = true;
                }
            }, this);
        },
        _findTarget: function (children, resourceId) {
            for (var i = 0; i < children.length; i++) {
                var child = children[i];
                if (child.resourceId === resourceId) {
                    return child.resourceId;
                } else if (child.properties && child.properties["overrideid"] === resourceId) {
                    return child.resourceId;
                } else {
                    var result = this._findTarget(child.childShapes, resourceId);
                    if (result) {
                        return result;
                    }
                }
            }
            return false;
        }
    });

    return new editorManager();
}]);

var flowableModule = flowableModeler;
var flowableApp = flowableModeler;

flowableModeler
    .config(['$provide', '$routeProvider', '$selectProvider', '$translateProvider', function ($provide, $routeProvider, $selectProvider, $translateProvider) {

        var appResourceRoot = FLOWABLE.CONFIG.webContextRoot + (FLOWABLE.CONFIG.webContextRoot ? '/' : '');
        $provide.value('appResourceRoot', appResourceRoot);

        // Override caret for bs-select directive
        angular.extend($selectProvider.defaults, {
            caretHtml: '&nbsp;<i class="icon icon-caret-down"></i>'
        });

        // Initialize angular-translate
        $translateProvider.useStaticFilesLoader({
            prefix: FLOWABLE.CONFIG.contextRoot + '/flow/editor-app/i18n/',
            suffix: '.json'
        });
        $translateProvider.preferredLanguage('zh-CN');
        // remember language
        $translateProvider.useCookieStorage();


    }])
    .run(['$rootScope', '$timeout', '$modal', '$translate', '$location', '$http', '$window', 'appResourceRoot', '$q', 'editorManager',
        function ($rootScope, $timeout, $modal, $translate, $location, $http, $window, appResourceRoot, $q, editorManager) {

            $rootScope.config = FLOWABLE.CONFIG;

            $rootScope.editorFactory = $q.defer();

            $rootScope.forceSelectionRefresh = false;

            $rootScope.ignoreChanges = false; // by default never ignore changes

            $rootScope.validationErrors = [];

            $rootScope.staticIncludeVersion = Date.now();


            // // set angular translate fallback language
            // $translate.fallbackLanguage(['en']);
            //
            // // setting Moment-JS (global) locale
            // if (FLOWABLE.CONFIG.datesLocalization) {
            //   moment.locale($translate.proposedLanguage());
            // }

            $rootScope.restRootUrl = function () {
                return FLOWABLE.CONFIG.contextRoot;
            };

            $rootScope.appResourceRoot = appResourceRoot;

            $rootScope.window = {};
            var updateWindowSize = function () {
                $rootScope.window.width = $window.innerWidth;
                $rootScope.window.height = $window.innerHeight;
            };

            // Window resize hook
            angular.element($window).bind('resize', function () {
                $rootScope.safeApply(updateWindowSize());
            });

            $rootScope.$watch('window.forceRefresh', function (newValue) {
                if (newValue) {
                    $timeout(function () {
                        updateWindowSize();
                        $rootScope.window.forceRefresh = false;
                    });
                }
            });

            updateWindowSize();


            /*
             * History of process and form pages accessed by the editor.
             * This is needed because you can navigate to sub processes and forms
             */
            $rootScope.editorHistory = [];

            /**
             * A 'safer' apply that avoids concurrent updates (which $apply allows).
             */
            $rootScope.safeApply = function (fn) {
                var phase = this.$root.$$phase;
                if (phase == '$apply' || phase == '$digest') {
                    if (fn && (typeof(fn) === 'function')) {
                        fn();
                    }
                } else {
                    this.$apply(fn);
                }
            };

            // Alerts
            $rootScope.alerts = {
                queue: []
            };

            $rootScope.showAlert = function (alert) {
                if (alert.queue.length > 0) {
                    alert.current = alert.queue.shift();
                    // Start timout for message-pruning
                    alert.timeout = $timeout(function () {
                        if (alert.queue.length == 0) {
                            alert.current = undefined;
                            alert.timeout = undefined;
                        } else {
                            $rootScope.showAlert(alert);
                        }
                    }, (alert.current.type == 'error' ? 5000 : 1000));
                } else {
                    $rootScope.alerts.current = undefined;
                }
            };

            $rootScope.addAlert = function (message, type) {
                var newAlert = {
                    message: message,
                    type: type
                };
                if (!$rootScope.alerts.timeout) {
                    // Timeout for message queue is not running, start one
                    $rootScope.alerts.queue.push(newAlert);
                    $rootScope.showAlert($rootScope.alerts);
                } else {
                    $rootScope.alerts.queue.push(newAlert);
                }
            };

            $rootScope.dismissAlert = function () {
                if (!$rootScope.alerts.timeout) {
                    $rootScope.alerts.current = undefined;
                } else {
                    $timeout.cancel($rootScope.alerts.timeout);
                    $rootScope.alerts.timeout = undefined;
                    $rootScope.showAlert($rootScope.alerts);
                }
            };

            $rootScope.addAlertPromise = function (promise, type) {
                if (promise) {
                    promise.then(function (data) {
                        $rootScope.addAlert(data, type);
                    });
                }
            };


            //editor


            /* Helper method to fetch model from server (always needed) */
            function fetchModel() {
                var modelId = "cc1a8b16-8de8-11e7-b39a-be8385e23d57";
                var modelUrl = FLOWABLE.URL.getModel(modelId);
                $http({
                    method: 'GET',
                    url: modelUrl
                }).success(function (data, status, headers, config) {
                    if (!data.defId) {
                        data = {
                            model: {
                                stencilset: {
                                    namespace: "http://b3mn.org/stencilset/bpmn2.0#"
                                }
                            },
                            name: "",
                            defKey: "",
                            desc: "",
                            typeId: "",
                            typeName: "",
                            defId: "",
                            reason: "",
                            version: ""
                        }
                    } else {
                        data.model = JSON.parse(data.model);
                    }
                    $rootScope.editor = new ORYX.Editor(data);
                    $rootScope.modelData = angular.fromJson(data);
                    $rootScope.editorFactory.resolve();

                    var childShapes = data.model.childShapes;
                    if (childShapes.length) {
                        for (var i = 0; i < childShapes.length; i++) {
                            $rootScope.addNewNodeNum(childShapes[i].stencil.id);
                        }
                    }
                }).error(function (data, status, headers, config) {
                    console.log('Error loading model with id ' + modelId + ' ' + data);
                });


                // var modelId = "";
                // var modelUrl;
                // if (modelId) {
                //   modelUrl = FLOWABLE.URL.getModel(modelId);
                // } else {
                //   modelUrl = FLOWABLE.URL.newModelInfo();
                // }
                //
                // $http({
                //   method: 'GET',
                //   url: modelUrl
                // }).
                // success(function(data, status, headers, config) {
                //   $rootScope.editor = new ORYX.Editor(data);
                //   $rootScope.modelData = angular.fromJson(data);
                //   $rootScope.editorFactory.resolve();
                // }).
                // error(function(data, status, headers, config) {
                //   $location.path("/processes/");
                // });
            }

            function initScrollHandling() {
                var canvasSection = jQuery('#canvasSection');
                canvasSection.scroll(function () {

                    // Hides the resizer and quick menu items during scrolling

                    var selectedElements = editorManager.getSelection();
                    var subSelectionElements = editorManager.getSubSelection();

                    $rootScope.selectedElements = selectedElements;
                    $rootScope.subSelectionElements = subSelectionElements;
                    if (selectedElements && selectedElements.length > 0) {
                        $rootScope.selectedElementBeforeScrolling = selectedElements[0];
                    }

                    jQuery('.Oryx_button').each(function (i, obj) {
                        $rootScope.orginalOryxButtonStyle = obj.style.display;
                        obj.style.display = 'none';
                    });
                    jQuery('.resizer_southeast').each(function (i, obj) {
                        $rootScope.orginalResizerSEStyle = obj.style.display;
                        obj.style.display = 'none';
                    });
                    jQuery('.resizer_northwest').each(function (i, obj) {
                        $rootScope.orginalResizerNWStyle = obj.style.display;
                        obj.style.display = 'none';
                    });
                    editorManager.handleEvents({
                        type: ORYX.CONFIG.EVENT_CANVAS_SCROLL
                    });
                });

                canvasSection.scrollStopped(function () {

                    // Puts the quick menu items and resizer back when scroll is stopped.

                    editorManager.setSelection([]); // needed cause it checks for element changes and does nothing if the elements are the same
                    editorManager.setSelection($rootScope.selectedElements, $rootScope.subSelectionElements);
                    $rootScope.selectedElements = undefined;
                    $rootScope.subSelectionElements = undefined;

                    function handleDisplayProperty(obj) {
                        if (jQuery(obj).position().top > 0) {
                            obj.style.display = 'block';
                        } else {
                            obj.style.display = 'none';
                        }
                    }

                    jQuery('.Oryx_button').each(function (i, obj) {
                        handleDisplayProperty(obj);
                    });
                    jQuery('.resizer_southeast').each(function (i, obj) {
                        handleDisplayProperty(obj);
                    });
                    jQuery('.resizer_northwest').each(function (i, obj) {
                        handleDisplayProperty(obj);
                    });

                });
            }

            jQuery.fn.scrollStopped = function (callback) {
                jQuery(this).scroll(function () {
                    var self = this,
                        $this = jQuery(self);
                    if ($this.data('scrollTimeout')) {
                        clearTimeout($this.data('scrollTimeout'));
                    }
                    $this.data('scrollTimeout', setTimeout(callback, 50, self));
                });
            };

            /**
             * Initialize the Oryx Editor when the content has been loaded
             */
            $rootScope.$on('$includeContentLoaded', function (event) {
                if (!$rootScope.editorInitialized) {

                    var paletteHelpWrapper = jQuery('#paletteHelpWrapper');
                    var paletteSectionFooter = jQuery('#paletteSectionFooter');
                    var paletteSectionOpen = jQuery('#paletteSectionOpen');
                    var contentCanvasWrapper = jQuery('#contentCanvasWrapper');

                    paletteSectionFooter.on('click', function () {
                        paletteHelpWrapper.addClass('close');
                        contentCanvasWrapper.addClass('collapsedCanvasWrapper');
                        paletteSectionOpen.removeClass('hidden');
                    });

                    paletteSectionOpen.on('click', function () {
                        paletteHelpWrapper.removeClass('close');
                        contentCanvasWrapper.removeClass('collapsedCanvasWrapper');
                        paletteSectionOpen.addClass('hidden');
                    });

                    /**
                     * A 'safer' apply that avoids concurrent updates (which $apply allows).
                     */
                    $rootScope.safeApply = function (fn) {
                        if (this.$root) {
                            var phase = this.$root.$$phase;
                            if (phase == '$apply' || phase == '$digest') {
                                if (fn && (typeof(fn) === 'function')) {
                                    fn();
                                }
                            } else {
                                this.$apply(fn);
                            }

                        } else {
                            this.$apply(fn);
                        }
                    };

                    $rootScope.addHistoryItem = function (resourceId) {
                        var modelMetaData = editorManager.getBaseModelData();

                        var historyItem = {
                            id: modelMetaData.modelId,
                            name: modelMetaData.name,
                            key: modelMetaData.key,
                            stepId: resourceId,
                            type: 'bpmnmodel'
                        };

                        if (editorManager.getCurrentModelId() != editorManager.getModelId()) {
                            historyItem.subProcessId = editorManager.getCurrentModelId();
                        }

                        $rootScope.editorHistory.push(historyItem);
                    };

                    /**
                     * Initialize the event bus: couple all Oryx events with a dispatch of the
                     * event of the event bus. This way, it gets much easier to attach custom logic
                     * to any event.
                     */

                    $rootScope.editorFactory.promise.then(function () {

                        $rootScope.formItems = undefined;

                        FLOWABLE.eventBus.editor = $rootScope.editor;

                        var eventMappings = [{
                            oryxType: ORYX.CONFIG.EVENT_SELECTION_CHANGED,
                            flowableType: FLOWABLE.eventBus.EVENT_TYPE_SELECTION_CHANGE
                        },
                            {
                                oryxType: ORYX.CONFIG.EVENT_DBLCLICK,
                                flowableType: FLOWABLE.eventBus.EVENT_TYPE_DOUBLE_CLICK
                            },
                            {
                                oryxType: ORYX.CONFIG.EVENT_MOUSEOUT,
                                flowableType: FLOWABLE.eventBus.EVENT_TYPE_MOUSE_OUT
                            },
                            {
                                oryxType: ORYX.CONFIG.EVENT_MOUSEOVER,
                                flowableType: FLOWABLE.eventBus.EVENT_TYPE_MOUSE_OVER
                            },
                            {
                                oryxType: ORYX.CONFIG.EVENT_EDITOR_INIT_COMPLETED,
                                flowableType: FLOWABLE.eventBus.EVENT_TYPE_EDITOR_READY
                            },
                            {
                                oryxType: ORYX.CONFIG.EVENT_PROPERTY_CHANGED,
                                flowableType: FLOWABLE.eventBus.EVENT_TYPE_PROPERTY_VALUE_CHANGED
                            }

                        ];

                        eventMappings.forEach(function (eventMapping) {
                            editorManager.registerOnEvent(eventMapping.oryxType, function (event) {
                                FLOWABLE.eventBus.dispatch(eventMapping.flowableType, event);
                            });
                        });

                        // Show getting started if this is the first time (boolean true for use local storage)
                        // FLOWABLE_EDITOR_TOUR.gettingStarted($rootScope, $translate, $q, true);
                    });

                    // Hook in resizing of main panels when window resizes
                    // TODO: perhaps move to a separate JS-file?
                    jQuery(window).resize(function () {

                        // Calculate the offset based on the bottom of the module header
                        var offset = jQuery("#editor-header").offset();
                        var propSectionHeight = jQuery('#propertySection').height();
                        var canvas = jQuery('#canvasSection');
                        var mainHeader = jQuery('#main-header');

                        if (offset == undefined || offset === null ||
                            propSectionHeight === undefined || propSectionHeight === null ||
                            canvas === undefined || canvas === null || mainHeader === null) {
                            return;
                        }

                        if ($rootScope.editor) {
                            var selectedElements = editorManager.getSelection();
                            var subSelectionElements = editorManager.getSelection();

                            $rootScope.selectedElements = selectedElements;
                            $rootScope.subSelectionElements = subSelectionElements;
                            if (selectedElements && selectedElements.length > 0) {
                                $rootScope.selectedElementBeforeScrolling = selectedElements[0];

                                editorManager.setSelection([]); // needed cause it checks for element changes and does nothing if the elements are the same
                                editorManager.setSelection($rootScope.selectedElements, $rootScope.subSelectionElements);
                                $rootScope.selectedElements = undefined;
                                $rootScope.subSelectionElements = undefined;
                            }
                        }

                        var totalAvailable = jQuery(window).height() - offset.top - mainHeader.height() - 21;
                        canvas.height(totalAvailable - propSectionHeight);
                        var footerHeight = jQuery('#paletteSectionFooter').height();
                        var treeViewHeight = jQuery('#process-treeview-wrapper').height();
                        jQuery('#paletteSection').height(totalAvailable - treeViewHeight - footerHeight);

                        // Update positions of the resize-markers, according to the canvas

                        var actualCanvas = null;
                        if (canvas && canvas[0].children[1]) {
                            actualCanvas = canvas[0].children[1];
                        }

                        var canvasTop = canvas.position().top;
                        var canvasLeft = canvas.position().left;
                        var canvasHeight = canvas[0].clientHeight;
                        var canvasWidth = canvas[0].clientWidth;
                        var iconCenterOffset = 8;
                        var widthDiff = 0;

                        var actualWidth = 0;
                        if (actualCanvas) {
                            // In some browsers, the SVG-element clientwidth isn't available, so we revert to the parent
                            actualWidth = actualCanvas.clientWidth || actualCanvas.parentNode.clientWidth;
                        }

                        if (actualWidth < canvas[0].clientWidth) {
                            widthDiff = actualWidth - canvas[0].clientWidth;
                            // In case the canvas is smaller than the actual viewport, the resizers should be moved
                            canvasLeft -= widthDiff / 2;
                            canvasWidth += widthDiff;
                        }

                        var iconWidth = 17;
                        var iconOffset = 20;

                        var north = jQuery('#canvas-grow-N');
                        north.css('top', canvasTop + iconOffset + 'px');
                        north.css('left', canvasLeft - 10 + (canvasWidth - iconWidth) / 2 + 'px');

                        var south = jQuery('#canvas-grow-S');
                        south.css('top', (canvasTop + canvasHeight - iconOffset - iconCenterOffset) + 'px');
                        south.css('left', canvasLeft - 10 + (canvasWidth - iconWidth) / 2 + 'px');

                        var east = jQuery('#canvas-grow-E');
                        east.css('top', canvasTop - 10 + (canvasHeight - iconWidth) / 2 + 'px');
                        east.css('left', (canvasLeft + canvasWidth - iconOffset - iconCenterOffset) + 'px');

                        var west = jQuery('#canvas-grow-W');
                        west.css('top', canvasTop - 10 + (canvasHeight - iconWidth) / 2 + 'px');
                        west.css('left', canvasLeft + iconOffset + 'px');

                        north = jQuery('#canvas-shrink-N');
                        north.css('top', canvasTop + iconOffset + 'px');
                        north.css('left', canvasLeft + 10 + (canvasWidth - iconWidth) / 2 + 'px');

                        south = jQuery('#canvas-shrink-S');
                        south.css('top', (canvasTop + canvasHeight - iconOffset - iconCenterOffset) + 'px');
                        south.css('left', canvasLeft + 10 + (canvasWidth - iconWidth) / 2 + 'px');

                        east = jQuery('#canvas-shrink-E');
                        east.css('top', canvasTop + 10 + (canvasHeight - iconWidth) / 2 + 'px');
                        east.css('left', (canvasLeft + canvasWidth - iconOffset - iconCenterOffset) + 'px');

                        west = jQuery('#canvas-shrink-W');
                        west.css('top', canvasTop + 10 + (canvasHeight - iconWidth) / 2 + 'px');
                        west.css('left', canvasLeft + iconOffset + 'px');
                    });

                    jQuery(window).trigger('resize');

                    FLOWABLE.eventBus.addListener('ORYX-EDITOR-LOADED', function () {
                        this.editorFactory.resolve();
                        this.editorInitialized = true;
                        this.modelData = editorManager.getBaseModelData();

                    }, $rootScope);


                    FLOWABLE.eventBus.addListener(FLOWABLE.eventBus.EVENT_TYPE_EDITOR_READY, function () {
                        var url = window.location.href;
                        var regex = new RegExp("[?&]subProcessId(=([^&#]*)|&|#|$)");
                        var results = regex.exec(url);
                        if (results && results[2]) {
                            editorManager.edit(decodeURIComponent(results[2].replace(/\+/g, " ")));
                        }
                    });

                    // Always needed, cause the DOM element on wich the scroll event listeners are attached are changed for every new model
                    initScrollHandling();
                    // var urlAry = $location.absUrl().split('/');
                    // var modelId = urlAry[urlAry.length - 2];
                    var modelId = getUrlParam('modelId');

                    editorManager.setModelId(modelId);
                    //fetchModel(modelId);
                    $http.get(FLOWABLE.CONFIG.contextRoot + "/flow/editor-app/stencilset_bpmn.json").then(function (response) {
                        var baseUrl = "http://b3mn.org/stencilset/";
                        editorManager.setStencilData(response.data);
                        //the stencilset alters the data ref!
                        var stencilSet = new ORYX.Core.StencilSet.StencilSet(baseUrl, response.data);
                        ORYX.Core.StencilSet.loadStencilSet(baseUrl, stencilSet, modelId);
                        //after the stencilset is loaded we make sure the plugins.xml is loaded.
                        return $http.get(FLOWABLE.CONFIG.contextRoot + "/flow/editor-app/plugins.xml");
                    }).then(function (response) {
                        ORYX._loadPlugins(response.data);
                        return $http.get(FLOWABLE.URL.getModel(modelId));
                    }).then(function (response) {
                        editorManager.bootEditor(response);
                    }).catch(function (error) {
                        console.log(error);
                    });

                    //minihack to make sure mousebind events are processed if the modeler is used in an iframe.
                    //selecting an element and pressing "del" could sometimes not trigger an event.
                    jQuery(window).focus();
                    $rootScope.editorInitialized = true;

                    $rootScope.$on('$locationChangeStart', function (event, next, current) {
                        if ($rootScope.editor && !$rootScope.ignoreChanges) {
                            var plugins = $rootScope.editor.loadedPlugins;

                            var savePlugin;
                            for (var i = 0; i < plugins.length; i++) {
                                if (plugins[i].type == 'ORYX.Plugins.Save') {
                                    savePlugin = plugins[i];
                                    break;
                                }
                            }

                            if (savePlugin && savePlugin.hasChanges()) {
                                // Always prevent location from changing. We'll use a popup to determine the action we want to take
                                event.preventDefault();

                                if (!$rootScope.unsavedChangesModalInstance) {

                                    var handleResponseFunction = function (discard) {
                                        $rootScope.unsavedChangesModalInstance = undefined;
                                        if (discard) {
                                            $rootScope.ignoreChanges = true;
                                            $location.url(next.substring(next.indexOf('/#') + 2));
                                        } else {
                                            $rootScope.ignoreChanges = false;
                                            $rootScope.setMainPageById('processes');
                                        }
                                    };

                                    $rootScope.handleResponseFunction = handleResponseFunction;

                                    _internalCreateModal({
                                        template: 'editor-app/popups/unsaved-changes.html',
                                        scope: $rootScope
                                    }, $modal, $rootScope);
                                }
                            }
                        }
                    });
                }
            });
        }
    ])
    // .run(['$rootScope', '$location', '$translate', '$window', '$modal',
    //   function($rootScope, $location, $translate, $window, $modal) {
    //
    //     var fixedUrlPart = '/editor/';
    //
    //     $rootScope.backToLanding = function() {
    //       var baseUrl = $location.absUrl();
    //       var index = baseUrl.indexOf(fixedUrlPart);
    //       if (index >= 0) {
    //         baseUrl = baseUrl.substring(0, index);
    //         baseUrl += '/';
    //       }
    //       $window.location.href = baseUrl;
    //     };
    //   }
    // ])

    // Moment-JS date-formatting filter
    .filter('dateformat', function () {
        return function (date, format) {
            if (date) {
                if (format) {
                    return moment(date).format(format);
                } else {
                    return moment(date).calendar();
                }
            }
            return '';
        };
    });
