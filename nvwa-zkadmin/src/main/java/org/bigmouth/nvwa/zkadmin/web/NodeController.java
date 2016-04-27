package org.bigmouth.nvwa.zkadmin.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.zookeeper.common.PathUtils;
import org.bigmouth.nvwa.network.http.response.JsonResponseSupport;
import org.bigmouth.nvwa.zkadmin.entity.Node;
import org.bigmouth.nvwa.zkadmin.service.NodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class NodeController extends JsonResponseSupport {

    private static final Logger LOGGER = LoggerFactory.getLogger(NodeController.class);
    
    @Autowired
    private NodeService nodeService;
    
    @RequestMapping(value = "path")
    public void path(HttpServletRequest request, HttpServletResponse response, String path) {
        if (StringUtils.isBlank(path)) {
            doFail(response, "Parameter 'path' has blank!");
            return;
        }
        try {
            if (!StringUtils.equals(path, "/")) {
                path = StringUtils.removeEnd(path, "/");
            }
            PathUtils.validatePath(path);
            Node node = nodeService.getNode(path);
            doSuccess(response, node);
        }
        catch (Exception e) {
            LOGGER.error("path:", e);
            doFail(response, "error");
            return;
        }
    }
    
    @RequestMapping(value = "delete")
    public void delete(HttpServletRequest request, HttpServletResponse response, String path) {
        if (StringUtils.isBlank(path)) {
            doFail(response, "Parameter 'path' has blank!");
            return;
        }
        try {
            if (!StringUtils.equals(path, "/")) {
                path = StringUtils.removeEnd(path, "/");
            }
            PathUtils.validatePath(path);
            nodeService.delete(path);
            doSuccess(response);
        }
        catch (Exception e) {
            LOGGER.error("delete:", e);
            doFail(response, "error");
            return;
        }
    }
    
    @RequestMapping(value = "save")
    public void delete(HttpServletRequest request, HttpServletResponse response, String path, String data) {
        if (StringUtils.isBlank(path)) {
            doFail(response, "Parameter 'path' has blank!");
            return;
        }
        try {
            if (!StringUtils.equals(path, "/")) {
                path = StringUtils.removeEnd(path, "/");
            }
            PathUtils.validatePath(path);
            nodeService.save(path, data);
            doSuccess(response);
        }
        catch (Exception e) {
            LOGGER.error("save:", e);
            doFail(response, "error");
            return;
        }
    }
    
    public void setNodeService(NodeService nodeService) {
        this.nodeService = nodeService;
    }
}
