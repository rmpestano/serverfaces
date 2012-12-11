/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.serverfaces.manager.bean;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.event.ItemSelectEvent;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.serverfaces.common.model.Server;
import org.serverfaces.manager.util.MessagesController;

/**
 *
 * @author Rafael M. Pestano - Dec 9, 2012 8:52:58 AM
 */
@Named
@SessionScoped
public class StatisticsMBean implements Serializable{

    @Inject ManagerMBean managerMBean;
    
     private CartesianChartModel serverMemoryModel;  
     private CartesianChartModel serverTransactionModel;  
     private CartesianChartModel serverRequestModel;  
     private CartesianChartModel serverCpuModel;  
     private List<Server> servers;
     
     @Inject
     MessagesController messages;
     
     public void doStatistics() throws IOException{
         managerMBean.doMonitoring();
         servers = managerMBean.getServers();
         if(servers == null || servers.isEmpty()){
             String baseUrl = ((HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest()).getContextPath();
             messages.addError("No server to get statistics from.<a href='"+ baseUrl +"/server.faces'> Click here</a> to add servers.");
             return;
         }
         serverMemoryModel = new CartesianChartModel();
         serverCpuModel = new CartesianChartModel();
         serverTransactionModel = new CartesianChartModel();
         serverRequestModel = new CartesianChartModel();
         for (Server server : servers) {
             ChartSeries memorySerie = new ChartSeries(server.getName());
             memorySerie.set("Used", server.getUsedMemory()/1000);
             memorySerie.set("Avaiable", server.getAvailableMemory()/1000);
             serverMemoryModel.addSeries(memorySerie);
             
             ChartSeries cpuSerie = new ChartSeries(server.getName());
             cpuSerie.set("Avg Resp.", server.getAvgResponseTime());
             cpuSerie.set("Max Resp", server.getMaxResponseTime());
             cpuSerie.set("Cpu Time", server.getCpuTime());
             serverCpuModel.addSeries(cpuSerie);
             
             ChartSeries transactionSerie = new ChartSeries(server.getName());
             transactionSerie.set("Active", server.getActiveTransactions());
             transactionSerie.set("Commited", server.getCommitedTransactions());
             transactionSerie.set("Rollback", server.getRollbackTransactions());
             serverTransactionModel.addSeries(transactionSerie);
             
             ChartSeries requestSerie = new ChartSeries(server.getName());
             requestSerie.set("Total", server.getTotalRequests());
             requestSerie.set("Succeed", server.getTotalRequests() - server.getTotalErrors());
             requestSerie.set("Errors", server.getTotalErrors());
             serverRequestModel.addSeries(requestSerie);
         }
     }

    public CartesianChartModel getServerMemoryModel() {
        return serverMemoryModel;
    }

    public void setServerMemoryModel(CartesianChartModel serverMemoryModel) {
        this.serverMemoryModel = serverMemoryModel;
    }

    public CartesianChartModel getServerTransactionModel() {
        return serverTransactionModel;
    }

    public void setServerTransactionModel(CartesianChartModel serverTransactionModel) {
        this.serverTransactionModel = serverTransactionModel;
    }

    public CartesianChartModel getServerRequestModel() {
        return serverRequestModel;
    }

    public void setServerRequestModel(CartesianChartModel serverRequestModel) {
        this.serverRequestModel = serverRequestModel;
    }

    public CartesianChartModel getServerCpuModel() {
        return serverCpuModel;
    }

    public void setServerCpuModel(CartesianChartModel serverCpuModel) {
        this.serverCpuModel = serverCpuModel;
    }

    public List<Server> getServers() {
        return servers;
    }

    public void setServers(List<Server> servers) {
        this.servers = servers;
    }

     
     
    
}
