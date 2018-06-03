/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customercrud.ejb;

import customercrud.entity.Customer;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author guzman
 */
@Stateless
public class CustomerFacade extends AbstractFacade<Customer> {

    @PersistenceContext(unitName = "CustomerCRUD2-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CustomerFacade() {
        super(Customer.class);
    }
    
    public List<Customer> buscarPorNombre (String cadena) {
        Query q = this.em.createQuery("select c from Customer c where c.name like :str");
        q.setParameter("str", "%" + cadena + "%");
        return q.getResultList();              
    }
    
    public List<Customer> buscarPorNombreYPorSupermercado (String cadena, String codigopostal) {
        Query q = this.em.createQuery("select c from Customer c " +
                                   "where c.name like :str and c.zip.zipCode = :cp");
        q.setParameter("str", "%" + cadena + "%");
        q.setParameter("cp", codigopostal);
        return q.getResultList();              
    }    
    
    
    // ESTO NUNCA DEBE HACERSE ASÍ. LOS GESTORES DE BASES DE DATOS DEBEN
    // TENER CAMPOS AUTOINCREMENTADOS
    // ESTA SOLUCIÓN ES MUY INEFICIENTE !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    public Integer buscarCustomerIdSiguiente () {
        Integer resultado;
        
        try {
            Query q = this.em.createQuery("select max(c.customerId) from Customer c");
            resultado = (Integer)q.getSingleResult();
            if (resultado == null) {
                resultado = new Integer(0);
            } else {
                resultado++;            
            }
        } catch(NoResultException ex) {
            resultado = 0;
        }
        return resultado;            
    }
    
    public void borrar (Integer clave) {
        Customer cliente;
        
        cliente = this.find(clave);
        this.remove(cliente);        
    }
    
}
