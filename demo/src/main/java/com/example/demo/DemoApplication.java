package com.example.demo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class DemoApplication { 
	
	private final List<Table> list = new ArrayList<Table>();
	private final List<Attendee> attendees = new ArrayList<Attendee>();
	private static final File file = new File("eurolandTest.html");
	private Document doc;
		
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
	
	@EventListener(ApplicationReadyEvent.class)
	public void runMeUponReady() {
		try {
			doc = Jsoup.parse(file, "UTF-8");
			extractHTMLTableData();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void extractHTMLTableData() {
		int row = 0;
		for(Element element : doc.select("tr")) {
			for(int i =0; i< element.childNodeSize(); i++) {
				Table table = new Table(row, i, element.childNode(i).childNode(0).toString());
				list.add(table);
			}
			
			if(row>0) {
				attendees.add(new Attendee(element.childNode(0).childNode(0).toString(), 
						element.childNode(1).childNode(0).toString(), 
						element.childNode(2).childNode(0).toString()));
			}
			
			row++;
		}			
	}
	
	@GetMapping("/table")
	public List<Table> getTable() {
		return list;
	}
	
	@GetMapping("/dimension")
	public TableDimension getTableDimention() {
		Elements rows =  doc.select("tr");
		return new TableDimension(rows.size(), rows.first().childNodeSize());
	}
	
	@GetMapping("/attendees")
	public List<Attendee> getAttendees() {
		Comparator<Attendee> sortByLastLetterOfNameDesc = new Comparator<Attendee>() {

			@Override
			public int compare(Attendee o1, Attendee o2) {
				return o2.getName().substring(o2.getName().length() -1).compareTo(o1.getName().substring(o1.getName().length()-1));
			}
			
		};
		Comparator<Attendee> attendeesSort = Comparator.comparing(Attendee::getBatch)
																					.thenComparing(Attendee::getCLASS)
																					.thenComparing(sortByLastLetterOfNameDesc);
		
		return attendees.stream().sorted(attendeesSort).collect(Collectors.toList());
	}
	
	@GetMapping("/groups")
	public Map<String, Number4> groupSortListCount() {
		Map<String, Number4> test = new HashMap<String, Number4>();
		
		 Map<String, List<Attendee>> mapBatchAttendees =  attendees.stream().sorted(Comparator.comparing(Attendee::getBatch).reversed())
				.collect(Collectors.groupingBy(Attendee::getBatch, Collectors.toList()));
		
		 mapBatchAttendees.forEach((k,v) -> {
			test.put(k, new Number4(v, v.size()));
		 });
		
		return test;
	}
	
	@PostMapping("/find")
	public Attendee findRecord(@RequestParam(name = "col1") String name, @RequestParam(name = "col2") String CLASS) {
		return attendees.stream().filter(a->a.getName().equals(name)).filter(a->a.getCLASS().equals(CLASS)).findFirst().get();
	}
	
	
}
