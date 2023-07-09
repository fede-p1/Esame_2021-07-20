package it.polito.tdp.yelp.model;

import java.util.HashMap;
import java.util.*;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.yelp.db.YelpDao;
import it.polito.tdp.yelp.model.Event.EventType;

public class Model { //10.15 - 10.58(1st)
	
	YelpDao dao;
	SimpleWeightedGraph<User,DefaultWeightedEdge> graph;
	Map<String,User> usersMap;
	Map<String, Business> businessMap;
	List<User> usersList;
	
	public Model() {
		dao = new YelpDao();
	}
	
	public SimpleWeightedGraph<User,DefaultWeightedEdge> creaGrafo(int n, int anno){
		
		graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		usersMap = new HashMap<>();
		
		for (User u : dao.getAllUsers())
			usersMap.put(u.getUserId(), u);
		
		List<User> vertex = new ArrayList<>(dao.getUsers(usersMap, n));
		
		Graphs.addAllVertices(graph, vertex);
		
		businessMap = new HashMap<>();
		
		for (Business b : dao.getAllBusiness())
			businessMap.put(b.getBusinessId(), b);
		
		dao.setBusinesses(usersMap, anno, businessMap);
		
		for (User u1 : graph.vertexSet())
			for (User u2 : graph.vertexSet())
				if (!u1.equals(u2) && !graph.containsEdge(u2, u1)) {
					Set<Business> intersection = new HashSet<>(u1.getBusinessList());
					intersection.retainAll(u2.getBusinessList());
					if (intersection.size()>0)
						Graphs.addEdge(graph, u1, u2, intersection.size());
				}
		
		return graph;
		
	}
	
	double pesoMax;

	public double getPesoMax() {
		return pesoMax;
	}

	public Set<User> getSimili(User u){
		
		pesoMax = 0.0;
		
		Set<User> simili = new HashSet<>();
		
		for (User uu : Graphs.neighborListOf(graph, u))
			if (graph.getEdgeWeight(graph.getEdge(uu, u)) > pesoMax)
				pesoMax = graph.getEdgeWeight(graph.getEdge(uu, u));
		
		for (User uu : Graphs.neighborListOf(graph, u))
			if (graph.getEdgeWeight(graph.getEdge(uu, u)) == pesoMax)
				simili.add(uu);
		
		return simili;
		
		
	}
	
	int lastDay;
	
	public int getLastDay() {
		return lastDay;
	}

	public void setLastDay(int lastDay) {
		this.lastDay = lastDay;
	}

	public Map<Integer,Integer> run(int nIntervistatori, int nUtenti) {
		
		usersList = new ArrayList<>(graph.vertexSet());
		
		PriorityQueue<Event> queue = new PriorityQueue<>();
		
		int time=0;
		while (nUtenti>0 && nIntervistatori>0) {
			time++;
			double prob = Math.random();
			EventType type;
			if (prob > 0.4)
				type = EventType.ASSEGNAZIONE;
			else if (prob < 0.2)
				type = EventType.FALLIMENTO;
			else
				type = EventType.FERIE;
			User u = usersList.get((int) (Math.random()*usersList.size()));
			queue.add(new Event(type,time,time,u ));
			nUtenti--;
			nIntervistatori--;
			usersList.remove(u);
		}
		
		lastDay = 0;
		Map<Integer,Integer> mapInterviste = new HashMap<>();
		
		for (int i=1; i<= time; i++)
			mapInterviste.put(i, 0);
		
		while (!queue.isEmpty()) {
			
			Event event = queue.poll();
			EventType type = event.getType();
			int intervistatore = event.getIntervistatore();
			lastDay = event.getDay();
			User user = event.getUser();
			
			switch(type) {
			case ASSEGNAZIONE:
				mapInterviste.put(intervistatore, mapInterviste.get(intervistatore)+1);
				User uu;
				if (this.getSimili(user).size()>0 ) {
					List<User> sim = new ArrayList<>(this.getSimili(user));
					sim.retainAll(usersList);
					uu = sim.get((int) (Math.random()*sim.size()));
					EventType typep;
					double prob = Math.random();
					if (prob > 0.4)
						typep = EventType.ASSEGNAZIONE;
					else if (prob < 0.2)
						typep = EventType.FALLIMENTO;
					else
						typep = EventType.FERIE;
					queue.add(new Event(typep,event.getDay()+1,intervistatore,uu));
					usersList.remove(uu);
				}
				else {
					uu = usersList.get((int) (Math.random()*(usersList.size()-1)));
					usersList.remove(uu);
					EventType typep;
					double prob = Math.random();
					if (prob > 0.4)
						typep = EventType.ASSEGNAZIONE;
					else if (prob < 0.2)
						typep = EventType.FALLIMENTO;
					else
						typep = EventType.FERIE;
					queue.add(new Event(typep,event.getDay()+1,intervistatore,uu));
				}
				break;
			case FERIE:
				mapInterviste.put(intervistatore, mapInterviste.get(intervistatore)+1);
				User uuu;
				if (this.getSimili(user).size()>0 ) {
					List<User> sim = new ArrayList<>(this.getSimili(user));
					sim.retainAll(usersList);
					uuu = sim.get((int) (Math.random()*(sim.size()-1)));
					EventType typep;
					double prob = Math.random();
					if (prob > 0.4)
						typep = EventType.ASSEGNAZIONE;
					else if (prob < 0.2)
						typep = EventType.FALLIMENTO;
					else
						typep = EventType.FERIE;
					queue.add(new Event(typep,event.getDay()+2,intervistatore,uuu));
					usersList.remove(uuu);
				}
				else {
					uuu = usersList.get((int) (Math.random()*(usersList.size()-1)));
					usersList.remove(uuu);
					EventType typep;
					double prob = Math.random();
					if (prob > 0.4)
						typep = EventType.ASSEGNAZIONE;
					else if (prob < 0.2)
						typep = EventType.FALLIMENTO;
					else
						typep = EventType.FERIE;
					queue.add(new Event(typep,event.getDay()+2,intervistatore,uuu));
				}
				break;
			case FALLIMENTO:
				EventType typep;
				double prob = Math.random();
				if (prob > 0.4)
					typep = EventType.ASSEGNAZIONE;
				else if (prob < 0.2)
					typep = EventType.FALLIMENTO;
				else
					typep = EventType.FERIE;
				queue.add(new Event(typep,event.getDay()+1,intervistatore,user));
				break;
			}
			
		}
		
		return mapInterviste;
		
		
	}

}
