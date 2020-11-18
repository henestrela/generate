package generator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class generate {

	String path = "C:/dev/generator";
	
	//comun
	String pascal = "StepDocumentGroupGenericQuestion"; // PascalCase  ExampleTesteApi 
	String lower = "stepdocumentgroupgenericquestion"; //lowercase exampleteste
	String camel = "stepDocumentGroupGenericQuestion";// camelCase exampleTeste
	
	//java
	String modulo = "Cfg"; // Cfg, Sys
	
	//yaml
	String name = "step document group generic question"; // lowercase example teste	
	String table = "CFG_STEP_DOC_GRP_GQ"; 
	
	Boolean hasNiveis = true;
	Integer niveis = 3;
	Integer enities = 2;
	List<String> properties = new ArrayList<String>();
	
	public generate() {
		properties.addAll(Arrays.asList(
		(new String[] 
				{	"id:string", 
					"codeAction:string",
					"flagAutoUpdate:boolean"
				}
		)));
	}
	
	

	public void generateJava() {
		File service = new File(path+"/templateService.txt");
		File serviceFinal = new File("C:/dev/"+pascal+"Service.Java");
		
		generateFinal(service, serviceFinal);
		
		File impl = new File(path+"/templateImpl.txt");
		File implFinal = new File("C:/dev/"+pascal+"ServiceImpl.Java");
		
		generateFinal(impl, implFinal);

		File repo = new File(path+"/templateRepo.txt");
		File repoFinal = new File("C:/dev/"+pascal+"Repository.Java");
		
		generateFinal(repo, repoFinal);
		
		File api = new File(path+"/templateApi.txt");
		File apiFinal = new File("C:/dev/"+pascal+"ApiController.Java");
		

		generateFinal(api, apiFinal);
	}
	
	
	public void generateFinal(File font, File fim) {
		String oldContent = "";

		BufferedReader reader = null;

		FileWriter writer = null;

		try {
			reader = new BufferedReader(new FileReader(font));

			// Reading all the lines of input text file into oldContent

			String line = reader.readLine();

			while (line != null) {
				oldContent = oldContent + line + System.lineSeparator();

				line = reader.readLine();
			}

			// Replacing oldString with newString in the oldContent

			String newContent = oldContent.replaceAll("#pascal#", pascal);
			newContent = newContent.replaceAll("#modulo#", modulo);
			newContent = newContent.replaceAll("#camel#", camel);	
			newContent = newContent.replaceAll("#lower#", lower);

			
			fim.createNewFile();
			writer = new FileWriter(fim);

			writer.write(newContent);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				// Closing the resources

				reader.close();

				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public String niveis(String space, int nivel ) {
		String s = new String(space);
		
		for(int i = 0; i< nivel; i++ ) {
			s = s+space;
		}
		return s;
	}
	
	public void generateYaml() {

	
		
		
		File fileToBeModified = new File(path+"/templateYaml.txt");
		File fileFinal = new File("C:/dev/"+camel+".yaml");

		String oldContent = "";

		BufferedReader reader = null;

		FileWriter writer = null;

		try {
			reader = new BufferedReader(new FileReader(fileToBeModified));

			// Reading all the lines of input text file into oldContent

			String line = reader.readLine();

			while (line != null) {
				oldContent = oldContent + line + System.lineSeparator();

				line = reader.readLine();
			}

			// Replacing oldString with newString in the oldContent

			String newContent = oldContent.replaceAll("#api#", pascal+"Api");
			newContent = newContent.replaceAll("#url#", camel);
			newContent = newContent.replaceAll("#name#", name);
			newContent = newContent.replaceAll("#auth#", lower);
			newContent = newContent.replaceAll("#writeAuth#", camel);
			newContent = newContent.replaceAll("#readAuth#", camel);
			newContent = newContent.replaceAll("#table#", table);
			newContent = newContent.replaceAll("#definition#", pascal);

			String propertySpace = "      ";
			String typeSpace  =     "        ";
			String doubleSpace  ="  ";
			
			String finalProperties = "";
			for(String s: properties) {
				String[] split = s.split(":");
				if(s.endsWith("string")) {
					finalProperties += propertySpace + split[0]+":\n";
					finalProperties += typeSpace + "type: " + split[1]+"\n";
				}
				if(s.endsWith("integer")) {
					finalProperties += propertySpace + split[0]+":\n";
					finalProperties += typeSpace + "type: " +  split[1]+"\n";
					finalProperties += typeSpace + "format: int32"+"\n";
				}
				if(s.endsWith("boolean")) {
					finalProperties += propertySpace + split[0]+":\n";
					finalProperties += typeSpace + "type: " + "string" +"\n";
					finalProperties += typeSpace + "enum: [ Y,N ]"+"\n";
				}
				if(s.endsWith("number")) {
					finalProperties += propertySpace + split[0]+":\n";
					finalProperties += typeSpace + "type: " + split[1]+"\n";
				}
				
			}
			if(hasNiveis) {
				for(int i = 1; i<= enities; i++) {
					finalProperties += propertySpace + "entity"+i+":\n";
					finalProperties += typeSpace + "type: object \n";
					finalProperties += typeSpace + "properties: \n";
					finalProperties += typeSpace + doubleSpace + "id: \n";
					finalProperties += typeSpace + doubleSpace + doubleSpace+ "type: integer \n";
					finalProperties += typeSpace + doubleSpace + doubleSpace+ "format: int32"+"\n";
					finalProperties += typeSpace + doubleSpace + "description: \n";
					finalProperties += typeSpace + doubleSpace + doubleSpace+ "type: string"+"\n";
					if(niveis > 1) {
						for(int j = 1; j< niveis; j++) {
							String space = niveis(doubleSpace, j);
							finalProperties += propertySpace + space + "nivel"+(j+1)+":\n";
							finalProperties += typeSpace + space  + "type: object \n";
							finalProperties += typeSpace + space  + "properties: \n";
							finalProperties += typeSpace + space  + doubleSpace + "id: \n";
							finalProperties += typeSpace + space  + doubleSpace + doubleSpace+ "type: integer \n";
							finalProperties += typeSpace + space  + doubleSpace + doubleSpace+ "format: int32"+"\n";
							finalProperties += typeSpace + space  + doubleSpace + "description: \n";
							finalProperties += typeSpace + space  + doubleSpace + doubleSpace+ "type: string"+"\n";
						}
					}
				}
			}
			
			newContent = newContent.replaceAll("#propeties#", finalProperties);
			// Rewriting the input text file with newContent
			
			fileFinal.createNewFile();
			writer = new FileWriter(fileFinal);

			writer.write(newContent);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				// Closing the resources

				reader.close();

				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
