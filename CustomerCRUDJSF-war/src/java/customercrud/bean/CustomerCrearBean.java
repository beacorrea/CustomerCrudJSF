/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customercrud.bean;

import customercrud.ejb.CustomerFacade;
import customercrud.ejb.DiscountCodeFacade;
import customercrud.ejb.MicroMarketFacade;
import customercrud.entity.Customer;
import customercrud.entity.DiscountCode;
import customercrud.entity.MicroMarket;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

/**
 *
 * @author beaco
 */
@Named(value = "customerCrearBean")
@RequestScoped
public class CustomerCrearBean {

    @Inject
    private CustomerBean customerBean;
    
    @EJB
    private DiscountCodeFacade discountCodeFacade;

    @EJB
    private MicroMarketFacade microMarketFacade;

    @EJB
    private CustomerFacade customerFacade;
    
    protected Customer cliente;
    protected String supermercadoSeleccionado;
    protected String descuentoSeleccionado;
    
    protected List<MicroMarket> listaSupermercados;
    protected List<DiscountCode> listaDescuentos;

    /**
     * Creates a new instance of CustomerCrearBean
     */
    public CustomerCrearBean() {
       
    }
    
    public Customer getCliente() {
        return cliente;
    }

    public void setCliente(Customer cliente) {
        this.cliente = cliente;
    }
      public List<MicroMarket> getListaSupermercados() {
        return listaSupermercados;
    }

    public void setListaSupermercados(List<MicroMarket> listaSupermercados) {
        this.listaSupermercados = listaSupermercados;
    }

    public List<DiscountCode> getListaDescuentos() {
        return listaDescuentos;
    }

    public void setListaDescuentos(List<DiscountCode> listaDescuentos) {
        this.listaDescuentos = listaDescuentos;
    }

    public String getSupermercadoSeleccionado() {
        return supermercadoSeleccionado;
    }

    public void setSupermercadoSeleccionado(String supermercadoSeleccionado) {
        this.supermercadoSeleccionado = supermercadoSeleccionado;
    }

    public String getDescuentoSeleccionado() {
        return descuentoSeleccionado;
    }

    public void setDescuentoSeleccionado(String descuentoSeleccionado) {
        this.descuentoSeleccionado = descuentoSeleccionado;
    }
    
    
    public String doGuardar(){
        MicroMarket superm = this.microMarketFacade.find(this.supermercadoSeleccionado);
        cliente.setZip(superm);
        
        DiscountCode dc = this.discountCodeFacade.find(this.descuentoSeleccionado);
        cliente.setDiscountCode(dc);
        
        if(cliente.getCustomerId() == null){
            cliente.setCustomerId(this.customerFacade.buscarCustomerIdSiguiente());
            this.customerFacade.create(cliente);
        }else{
            this.customerFacade.edit(cliente);
        }
        this.customerBean.init();
        return "index";
    }
    
    @PostConstruct
    public void init(){
        this.listaSupermercados = this.microMarketFacade.findAll();
        this.listaDescuentos = this.discountCodeFacade.findAll();
        if(this.customerBean.getIdCustomerSeleccionado() != -1){ //Editar
            this.cliente = this.customerFacade.find(this.customerBean.getIdCustomerSeleccionado());
            this.customerBean.setIdCustomerSeleccionado(-1);
            this.supermercadoSeleccionado = this.cliente.getZip().getZipCode();
            this.descuentoSeleccionado = this.cliente.getDiscountCode().getDiscountCode();
        }else{ //Nuevo cliente
             cliente = new Customer();
        }
        
    }
}
