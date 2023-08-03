
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

public class RomaniaClass {
	public static final Scanner scanner = new Scanner(System.in);
	public static void main(String args[]) {

		String goal = "Bucharest";
		//Store the map route information in HashMap
		Map<String, Map<String, Integer>> cityMapInfo = readData();


		//Ask user to print romania city map info and print the map
		displayRouteMap(cityMapInfo);

		//get valid city input from user
		String cityNmae= getValidCityInput(cityMapInfo);

		while(cityNmae.equals(goal)) {
			System.out.println("You are already in Bucharest !!! Please Enter different city");
			cityNmae= getValidCityInput(cityMapInfo);
		}
		boolean status = true;

		
		while (status) {
			int decision = searchMenu();

			if (decision == 1) {

				BFSnew(cityMapInfo, cityNmae, goal);

			} else if (decision == 2) {

				DFSNew(cityMapInfo, cityNmae, goal);

			} else if (decision == 3) {

				IDSsearch(cityMapInfo, cityNmae, goal);

			} else if (decision == 4) {
				cityNmae = getValidCityInput(cityMapInfo);
	;
				continue;
			} else if (decision == 5) {
				exitRomania();
				status = false;
			} else {
				printErrMsg();
				System.out.println();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				decision = searchMenu();
			}
		}

	}


	/**
	 * @param filename
	 * @return
	 */
	public static Map<String, Map<String, Integer>> readData() {
		Map<String, Map<String, Integer>> dictGraph = new HashMap<>();
		ArrayList<ArrayList<String>> cityList = Data();
		for(int i=0;i< cityList.size();i++) {
			ArrayList<String> cities = cityList.get(i);
			if (!dictGraph.containsKey(cities.get(0))) {
				dictGraph.put(cities.get(0), new TreeMap<>());
			}
			dictGraph.get(cities.get(0)).put(cities.get(1), 1);

			if (!dictGraph.containsKey(cities.get(1))) {
				dictGraph.put(cities.get(1), new TreeMap<>());
			}
			dictGraph.get(cities.get(1)).put(cities.get(0), 1);
		}
		return dictGraph;
	}

	/**
	 * @return
	 */
	public static ArrayList<ArrayList<String>> Data(){
		@SuppressWarnings("serial")
		ArrayList<ArrayList<String>> cityList = new ArrayList<>() {{
			add(new ArrayList<String>() {{ add("Oradea"); add("Zerind"); }});
			add(new ArrayList<String>() {{ add("Oradea"); add("Sibiu"); }});
			add(new ArrayList<String>() {{ add("Zerind"); add("Arad"); }});
			add(new ArrayList<String>() {{ add("Arad"); add("Sibiu"); }});
			add(new ArrayList<String>() {{ add("Arad"); add("Timisoara"); }});
			add(new ArrayList<String>() {{ add("Timisoara"); add("Lugoj"); }});
			add(new ArrayList<String>() {{ add("Lugoj"); add("Mehadia"); }});
			add(new ArrayList<String>() {{ add("Mehadia"); add("Drobeta"); }});
			add(new ArrayList<String>() {{ add("Drobeta"); add("Craiova"); }});
			add(new ArrayList<String>() {{ add("Sibiu"); add("RimnicuVilcea"); }});
			add(new ArrayList<String>() {{ add("Sibiu"); add("Fagaras"); }});
			add(new ArrayList<String>() {{ add("RimnicuVilcea"); add("Piteshi"); }});
			add(new ArrayList<String>() {{ add("RimnicuVilcea"); add("Craiova"); }});
			add(new ArrayList<String>() {{ add("Craiova"); add("Piteshi"); }});
			add(new ArrayList<String>() {{ add("Piteshi"); add("Bucharest"); }});
			add(new ArrayList<String>() {{ add("Fagaras"); add("Bucharest"); }});
			add(new ArrayList<String>() {{ add("Bucharest"); add("Giurgiu"); }});
			add(new ArrayList<String>() {{ add("Bucharest"); add("Urziceni"); }});
			add(new ArrayList<String>() {{ add("Urziceni"); add("Hirsova"); }});
			add(new ArrayList<String>() {{ add("Urziceni"); add("Vaslui"); }});
			add(new ArrayList<String>() {{ add("Hirsova"); add("Eforie"); }});
			add(new ArrayList<String>() {{ add("Vaslui"); add("Iasi"); }});
			add(new ArrayList<String>() {{ add("Neamt"); add("Iasi"); }});

		}};


		return cityList;

	}



	/**
	 * @param routeMap
	 */
	public static void displayRouteMap(Map<String, Map<String, Integer>> routeMap) {
		System.out.print("Display adjacent list represented by a dictionary? (Y/n): ");

		String userInput = null;
		userInput= scanner.nextLine();
		userInput= userInput.toLowerCase();


		while (!userInput.equals("n")) {
			if (userInput.equals("y")) {
				System.out.println();
				printMap(routeMap);
				/*printAdjacencyMatrixOfRomaniaMap();
				printAdjacencyListOfRomaniaMap();*/
				break;
			} else {
				System.out.print("Please enter (y/n): ");
				userInput = System.console().readLine().toLowerCase();
			}
		}

	}



	/**
	 * @param routeMap
	 */
	public static void printMap(Map<String, Map<String, Integer>> routeMap) {
		for (String key : routeMap.keySet()) {
			System.out.println(key + " -> " + routeMap.get(key));
		}
		System.out.println();
	}

	/**
	 * @param data
	 * @return
	 */
	public static String getValidCityInput(Map<String, Map<String, Integer>> data) {
		boolean state = true;
		String userInp="";

		while (state) {
			userInp = getSourceCity();
			state = isCity(userInp, data);
			if(!state) {
				printErrMsg();
				state=true;
			}else {
				break;
			}


		}
		return userInp;
	}

	/**
	 * @return
	 */
	public static String getSourceCity() {
		System.out.print("Source city: ");

		String root = scanner.nextLine();





		//root = root.substring(0, 1).toUpperCase() + root.substring(1);
		//System.out.print(root);

		return root;

	}

	/**
	 * @param root
	 * @param dictionary
	 * @return
	 */
	public static boolean isCity(String root, Map<String, Map<String, Integer>> dictionary) {
		boolean state = false;
		for (String key : dictionary.keySet()) {
			if (root.equals(key)) {
				state = true;
				System.out.printf("\nLet us begin the trip from %s to Bucharest!\n\n", root);
			}
		}
		return state;
	}

	/**
	 * 
	 */
	public static void printErrMsg() {
		System.out.println("\nInvalid Entry. Please try again.");
	}

	
	/**
	 * @return
	 */

	public static int searchMenu() {
		System.out.println("1. Breadth-First Search");
		System.out.println("2. Depth-First Search");
		System.out.println("3. Iterative-Deepening Search");
		System.out.println("4. New city");
		System.out.println("5. Exit Romania");
		System.out.print("Enter your choice: ");
		return scanner.nextInt();
	}	

	public static boolean keepGoing() {
		ArrayList<String> validInputs = new ArrayList<String>(Arrays.asList("y", "n"));


		System.out.print("\nWould you like to continue? (Y/n): ");
		String ans = scanner.nextLine().toLowerCase();

		while (true) {


			if (!validInputs.contains(ans)) {

				return keepGoing();

			} else if (ans.equals("y")) {
				break;
			} else if (ans.equals("n")) {
				return false;
			}
		}

		return true;
	}
	/**
	 * 
	 */
	public static void exitRomania() {
		System.out.println("Thank you for visiting Romania!\n");
		sleep(1);
		sleep(1);
		System.exit(0);
	}

	public static void sleep(int seconds) {
		try {
			TimeUnit.SECONDS.sleep(seconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}



	@SuppressWarnings("unchecked")
	public static void BFSnew(Map<String, Map<String, Integer>> graph, String start, String goal) 
	{   
		Map<String, Boolean> visited=null; // To check if cities are visited or not
		List<String> exploredCities= new ArrayList<String>(); // keep the track explored/expanded cities
		Map<String,String> parentMap = new HashMap<String,String>(); // use to find shortest path after exploring all cities
		try {
			visited = defaultVisited(graph); // this method will set all the cities as false 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//boolean visited[] = new boolean[graph.size()]; 

		LinkedList<String> queue = new LinkedList<String>(); //cities visited will be added in the queue/frontier
		String tempStart= start;
		visited.replace(tempStart, true);  
		queue.add(tempStart); 
		int cost=0;
		System.out.println("============BFS=============="+start+"==================BFS================");
		while (queue.size() != 0) 
		{ 
			System.out.println("Frontier looks like this now ->" + queue);
			tempStart = queue.poll(); 

			System.out.println("Exploring " + tempStart + "..." + tempStart +" is removed from Frontier");
			if(tempStart.equals(goal))System.out.println("Goal Bucharest is found !!!");
			//c= queue1.poll();
			exploredCities.add(tempStart);

			cost+=1;
			if(tempStart.equals(goal))break;  //if goal city is reached dont go further
			Map<String, Integer> temp=graph.get(tempStart);// It will return all child cities
			
			//tempList will have those child cities which are not explored
			String nodeTemp;
			ArrayList<String> tempList = new ArrayList<String>();
			for (Map.Entry<String, Integer> entry : temp.entrySet()) {

				nodeTemp= entry.getKey();

				if(!visited.get(nodeTemp)) {
					tempList.add(nodeTemp);

				}
			}
			
			
			
			if(!tempList.isEmpty() ) {
				System.out.println("Successors of " +tempStart+ " are " + temp + " However, Unexplored successors of " +tempStart + " which are "+tempList+ " added in queue in alphabetical order from back side of queue.....");

			}else {
				System.out.println(tempStart + " doesnt have any child or the childs are already explored , so exploring other cities");
			}
			String node;
			for (Map.Entry<String, Integer> entry : temp.entrySet()) {

				node= entry.getKey();

				if(!visited.get(node)) {
					parentMap.put(node, tempStart);
					visited.replace(node, true); 
					queue.add(node);

				}
			}


		}
		cost=cost-1;
		System.out.println("------------------------Results------------------------");
		System.out.println("Cities explored : " + exploredCities);
		System.out.println("BFS Path cost: "+cost);
		System.out.println("Shortest Path from Bucharest : "+(List<String>)(findShortestPath(parentMap,start,goal)).get("ShortestPath"));
		System.out.println("Cost of Shortest Path from Bucharest : "+(Integer)(findShortestPath(parentMap,start,goal)).get("CostOfShortestPath"));
		System.out.println("-------------------------------------------------------");
		System.out.println("===================================================================");


	}
	static Map<String,Boolean> defaultVisited(Map<String, Map<String, Integer>> graph){
		Map<String,Boolean> visited= new HashMap<String,Boolean>();
		Set<String> cities= graph.keySet();
		for (String element : cities) {
			visited.put(element,false);

		}

		return visited;
	}

	public static Map<String,Object> findShortestPath(Map<String,String> parentMap, String start, String goal ){
		Map<String,Object> returnMap =new HashMap<String, Object>();
		List<String> shortestPath= new ArrayList<String>();
		int cost =0;
		while (!start.equals(goal)) {
			shortestPath.add(goal);
			String tempParent = parentMap.get(goal);
			goal =tempParent;
			cost++;
		}
		shortestPath.add(start);
		Collections.reverse(shortestPath);
		returnMap.put("ShortestPath", shortestPath);
		returnMap.put("CostOfShortestPath", cost);
		return returnMap;



	}
	@SuppressWarnings("unchecked")
	public static void DFSNew(Map<String, Map<String, Integer>> graph, String start, String goal) 
	{ 

		Map<String, Boolean> visited=null;
		List<String> exploredCities= new ArrayList<String>();
		Map<String,String> parentMap = new HashMap<String,String>();
		try {
			visited = defaultVisited(graph);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Stack<String> stack = new Stack<>(); 
		String tempStart = start; 

		stack.push(tempStart);

		int cost=0;
		System.out.println("============DFS=============="+start+"==================DFS================");
		while(stack.empty() == false) 
		{   
			System.out.println("Frontier looks like this now ->");
			ArrayList<String> list = new ArrayList<>(stack);
		        ListIterator<String> iterator = list.listIterator(list.size());

		        while (iterator.hasPrevious()) {
		            String element = iterator.previous();
		            System.out.print(element + "   ");
		            
		        }
		    System.out.println();
			tempStart = stack.peek();

			stack.pop(); 

			cost+=1;

			if(visited.get(tempStart) == false) 
			{ 
				exploredCities.add(tempStart);
				visited.replace(tempStart, true); 
				System.out.println("Exploring " + tempStart + "..." + tempStart +" is removed from Frontier");
			} 
			if(tempStart.equals(goal))System.out.println("Goal Bucharest is found !!!");
			if(tempStart.equals(goal))break; 

			Map<String, Integer> sortedTemp=graph.get(tempStart);
			//System.out.println(temp);
			//Map<String, Integer> sortedTemp = new TreeMap<>(Collections.reverseOrder());
			// sortedTemp.putAll(temp);
			String nodeTemp;
			ArrayList<String> tempList = new ArrayList<String>();
			for (Map.Entry<String, Integer> entry : sortedTemp.entrySet()) {

				nodeTemp= entry.getKey();

				if(!visited.get(nodeTemp)) {
					tempList.add(nodeTemp);

				}
			}
			
			if(!tempList.isEmpty()) {
				System.out.println("Successors of " +tempStart + " are "+sortedTemp+ " However, unexplored successors of " + tempStart + " which are " + tempList + " added in queue in alphabetical order from front side of queue.....");

			}else {
				System.out.println(tempStart + " doesnt have any child or the childs are already explored , so exploring other cities");
			}
			String node;
			for (Map.Entry<String, Integer> entry : sortedTemp.entrySet()) {

				node= entry.getKey();

				if(!visited.get(node)) {
					parentMap.put(node, tempStart);

					stack.push(node);

				}
			}


		}
		cost=cost-1;
		System.out.println("------------------------Results------------------------");
		System.out.println("Cities explored : " + exploredCities);
		System.out.println("DFS Path cost: "+cost);
		System.out.println("Shortest Path from Bucharest : "+(List<String>)(findShortestPath(parentMap,start,goal)).get("ShortestPath"));
		System.out.println("Cost of Shortest Path from Bucharest : "+(Integer)(findShortestPath(parentMap,start,goal)).get("CostOfShortestPath"));	
		System.out.println("-------------------------------------------------------");
		System.out.println("===================================================================");

	}
    static int totalIdsCost= 0;
	public static void IDSsearch(Map<String, Map<String, Integer>> graph, String start, String goal) {
		boolean found = false;
		int depthLimit = 0;
		
		List<String> shortestPath = new ArrayList<>();
		while (!found) {
			Set<String> visited = new HashSet<>();
			found = dfsLimited(graph, start, goal, depthLimit, visited,shortestPath);
			

			System.out.println("Nodes explored at"+ " depth limit "+depthLimit+ "are " + exploredNode);
			totalIdsCost= exploredNode.size()-1+totalIdsCost;
			System.out.println("Total cost of IDS so far is " + totalIdsCost);
			depthLimit++;
			exploredNode.clear();
		}
		if (found) {
			Collections.reverse(shortestPath);
			System.out.println("Shortest Path: " + shortestPath);
			
			System.out.println("Cost of Shortest Path: " + (shortestPath.size()-1));
			totalIdsCost=0;
			exploredNode.clear();
		} else {
			System.out.println("No path found from " + start + " to " + goal);
		}
		totalIdsCost=0;
		exploredNode.clear();
		// System.out.println("Shortest Path from Bucharest : "+findShortestPath(parentMap,start,goal));
	}
	static List<String> exploredNode = new ArrayList<String>();
	

	public static boolean dfsLimited(Map<String, Map<String, Integer>> graph, String currentCity, String goalCity,
			int depthLimit, Set<String> visited, List<String> shortestPath) {
		if(!exploredNode.contains(currentCity)) {
			exploredNode.add(currentCity);
		}

		if (currentCity.equals(goalCity)) {
			System.out.println("Goal city reached: " + currentCity);
			shortestPath.add(currentCity);
			return true;
		}

		if (depthLimit == 0) {

			return false;
		}

		visited.add(currentCity);
        
		Map<String, Integer> neighbors = graph.getOrDefault(currentCity, new HashMap<>());
		for (String neighbor : neighbors.keySet()) {
			if (!visited.contains(neighbor)) {
				boolean found = dfsLimited(graph, neighbor, goalCity, depthLimit - 1, visited, shortestPath);
				if (found) {
					shortestPath.add(currentCity);
					return true;
				}
			}
		}

		visited.remove(currentCity);
		//System.out.println("Nodes explored at"+ " depth limit "+depthLimit+ "are " + exploredNode);
		return false;
	}
}
