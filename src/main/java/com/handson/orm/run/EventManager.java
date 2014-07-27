package com.handson.orm.run;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;

import com.handson.orm.bo.Event;
import com.handson.orm.bo.Person;
import com.handson.orm.util.HibernateUtil;

public class EventManager {

    public static void main(String[] args) {
        EventManager mgr = new EventManager();

        if (args[0].equals("store")) {
            mgr.createAndStoreEvent("My Event", new Date());
            
        } else if (args[0].equals("list")) {
        	
            for (Event event : mgr.listEvents()) {
            	System.out.println(event);
            }
            
        } else if (args[0].equals("addpersontoevent")) {
            Long eventId = mgr.createAndStoreEvent("My Event", new Date());
            Long personId = mgr.createAndStorePerson("Foo", "Bar", 18);
            mgr.addPersonToEvent(personId, eventId);
            System.out.println("Added person " + personId + " to event " + eventId);
            
        } else if (args[0].equals("listperson")) {
        	
        	System.out.println("------------------PERSONS ---------------");
            for (Person person : mgr.listPersons()) {
            	System.out.println(person);
            }
            
        }

        HibernateUtil.getSessionFactory().close();
    }
    
    
    
    

    private List<Event> listEvents() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		
		@SuppressWarnings("unchecked")
		List<Event> events = session.createQuery("from Event").list();
		
		session.getTransaction().commit();
		return events;
	}
    
    private List<Person> listPersons() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		
		@SuppressWarnings("unchecked")
		List<Person> persons = session.createQuery("from Person").list();
		
		session.getTransaction().commit();
		return persons;
	}

	private Long createAndStoreEvent(String title, Date date) {
      Session session = HibernateUtil.getSessionFactory().getCurrentSession();
      session.beginTransaction();
      
      Event event = new Event();
      event.setTitle(title);
      event.setDate(date);
      
      Long idEvent = (Long) session.save(event);
      session.getTransaction().commit();
      
      return idEvent;
      
    }
	
	private Long createAndStorePerson(String firstname, String lastname, int age) {
	      Session session = HibernateUtil.getSessionFactory().getCurrentSession();
	      session.beginTransaction();
	      
	      Person person = new Person();
	      person.setFirstname(firstname);
	      person.setLastname(lastname);
	      person.setAge(age);
	      
	      Long idPerson = (Long) session.save(person);
	      session.getTransaction().commit();
	      
	      return idPerson;
	      
	    }
	
	private void addPersonToEvent(Long personId, Long eventId) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        
        Person person = (Person) session.load(Person.class, personId);
        Event event = (Event) session.load(Event.class, eventId);
//        person.getEvents().add(event);
        
        
        session.getTransaction().commit();
        
        
        person.getEvents().add(event);
        
        
        
        Session session2 = HibernateUtil.getSessionFactory().getCurrentSession();
        session2.beginTransaction();
        session2.update(person); // Reattachment of person
        session2.getTransaction().commit();

    }

}
