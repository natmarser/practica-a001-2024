package ule.edi.travel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.*;

import ule.edi.model.*;

public class TravelArrayImplTests {

	private DateFormat dformat = null;
	private TravelArrayImpl e, ep;
	
	private Date parseLocalDate(String spec) throws ParseException {
        return dformat.parse(spec);
	}

	public TravelArrayImplTests() {
		
		dformat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	}
	
	@Before
	public void testBefore() throws Exception{
	    e = new TravelArrayImpl(parseLocalDate("24/02/2020 17:00:00"), 110);
	    ep = new TravelArrayImpl(parseLocalDate("24/02/2020 17:00:00"), 4);

	}
	
	@Test
	public void testEventoVacio() throws Exception {
		
	    Assert.assertTrue(e.getNumberOfAvailableSeats()==110);
	    Assert.assertEquals(110, e.getNumberOfAvailableSeats());
	    Assert.assertEquals(0, e.getNumberOfAdults());
	    Assert.assertEquals(0, e.getNumberOfChildren());
	    Assert.assertEquals(100.0,0.0, e.getPrice());
		  	
	}
	
	// test 2 constructor
	@Test
	public void test2Constructor() throws Exception{
		 TravelArrayImpl  e2 = new TravelArrayImpl(parseLocalDate("24/02/2020 17:00:00"), 110, 200.0, (byte) 20);
		 Assert.assertEquals(parseLocalDate("24/02/2020 17:00:00"), e2.getTravelDate());

	    Assert.assertEquals( 200.0,0.0, e2.getPrice());
	    Assert.assertEquals((byte)20,(byte) e2.getDiscountAdvanceSale());
	}
	
	
	@Test
	public void test2ConstructorCollect() throws Exception{
		 TravelArrayImpl  e2 = new TravelArrayImpl(parseLocalDate("24/02/2018 17:00:00"), 110, 200.0, (byte) 20);
		 Assert.assertTrue(e2.sellSeatPos(1, "10203040A","Alice", 34,false));	//venta normal
		 Assert.assertTrue(e2.sellSeatPos(2, "10203040B","Alice", 34,true));	//venta anticipada
		 Assert.assertEquals(2, e2.getNumberOfSoldSeats());	
						 
	    Assert.assertEquals(360.0,0.0,e2.getCollectionTravel());
		}
	  
	// test getDiscountAdvanceSale
	
	@Test
	public void testGetDiscountAdvanceSale() throws Exception {
		
	    Assert.assertTrue(e.getDiscountAdvanceSale()==25);
	}
	
	// test getDate
	
	@Test
	public void testGetDate() throws Exception {
		
	    Assert.assertEquals(parseLocalDate("24/02/2020 17:00:00"), e.getTravelDate());
	    Assert.assertEquals(110,e.getNumberOfAvailableSeats());
	    Assert.assertEquals(0, e.getNumberOfAdults());
	    Assert.assertEquals(0, e.getNumberOfSoldSeats());	
		
	}
	
	// test getNumber....
	@Test
	public void testsellSeatPos1Adult() throws Exception{	
	    Assert.assertEquals(0, e.getNumberOfAdults());
		Assert.assertTrue(e.sellSeatPos(4, "10203040A","Alice", 18,false));	//venta normal
	    Assert.assertEquals(1,e.getNumberOfAdults());  
	    Assert.assertEquals(0,e.getNumberOfAdvanceSaleSeats());	
	    Assert.assertEquals(1,e.getNumberOfNormalSaleSeats());  
	    Assert.assertEquals(1,e.getNumberOfSoldSeats());	
	    Assert.assertEquals(110,e.getNumberOfSeats());  
	   
	}
	
	
	// TEST OF sellSeatPos
	@Test
	public void testsellSeatPosPosCero() throws Exception{		
	   Assert.assertEquals(false,e.sellSeatPos(0, "10203040A","Alice", 34,false));	//venta normal  
	}
	
	@Test
	public void testsellSeatPosPosMayorMax() throws Exception{		
	   Assert.assertEquals(false,e.sellSeatPos(e.getNumberOfAvailableSeats()+1, "10203040A","Alice", 34,false));	//venta normal  
	}
	@Test
	public void testsellSeatPosPosOcupada() throws Exception{		
	   Assert.assertEquals(true, e.sellSeatPos(5, "10203040A","Alice", 34,false));	//venta normal  
	   Assert.assertEquals(false, e.sellSeatPos(5, "10203040A","Alice", 34,false));	//venta normal  
	}
	@Test
	public void testsellNifRepetido() throws Exception{
		Assert.assertEquals(true , e.sellSeatPos(6,"10203040A","Alice", 34,true));	//venta anticipada
		Assert.assertEquals(true, ep.sellSeatPos(2,"10203040A", "Alice", 34, false)); //venta normal
		Assert.assertEquals(false, e.sellSeatPos(9, "10203040A", "Alic", 38, false)); //venta normal
		Assert.assertEquals(false, ep.sellSeatPos(4,"10203040A", "Alicia", 18, true)); //venta anticipada
		Assert.assertEquals(false, ep.sellSeatPos(2, "123456N", "Maria", 10, false)); //venta normal
	}
	

	//TEST OF sellSeatFrontPos
	@Test
	public void testsellSeatFront() throws Exception{		
	   Assert.assertEquals(1, e.sellSeatFrontPos("10203040A","Alice", 34,false));	//venta normal
	   Assert.assertEquals(2, e.sellSeatFrontPos("10203040B", "Alice", 34, false));
	   Assert.assertEquals(1, ep.sellSeatFrontPos("10203040A", "Alice", 34, false));
	}

	@Test
	public void testsellSeatFrontPosNifRepetido() throws Exception{
		Assert.assertEquals(1 , e.sellSeatFrontPos("10203040A","Alice", 34,true));	//venta anticipada
		Assert.assertEquals(1, ep.sellSeatFrontPos("10203040A", "Alice", 34, false)); //venta normal
		Assert.assertEquals(-1, e.sellSeatFrontPos("10203040A", "Alice", 34, false)); //venta normal
		Assert.assertEquals(-1, ep.sellSeatFrontPos("10203040A", "Alice", 34, true));//venta anticipada
	}

	@Test
	public void testsellSeatFrontLleno() throws Exception{
		Assert.assertEquals(1 , ep.sellSeatFrontPos("10203040A","Alice", 34,true));	//venta anticipada
		Assert.assertEquals(2, ep.sellSeatFrontPos("10203040B", "Alice", 34, false)); //venta normal
		Assert.assertEquals(3, ep.sellSeatFrontPos("10203040C", "Alice", 34, false)); //venta normal
		Assert.assertEquals(4, ep.sellSeatFrontPos("10203040D", "Alice", 34, true)); //venta anticipada
		Assert.assertEquals(-1, ep.sellSeatFrontPos("10203040E", "Alice", 34, false)); //venta normal 
	}




	//TEST OF sellSeatRearPos
	@Test
	public void testsellSeatRear() throws Exception{		
	   Assert.assertEquals(110 , e.sellSeatRearPos("10203040A","Alice", 34,false));	//venta normal
	   Assert.assertEquals(109, e.sellSeatRearPos("10203040B", "Alice", 34, false));
	   Assert.assertEquals(4, ep.sellSeatRearPos("10203040A", "Alice", 34, false));
	}

	@Test
	public void testsellSeatRearPosNifRepetido() throws Exception{
		Assert.assertEquals(110, e.sellSeatRearPos("10203040A","Alice", 34,true));	//venta anticipada
		Assert.assertEquals(4, ep.sellSeatRearPos("10203040A", "Alice", 34, false)); //venta normal
		Assert.assertEquals(-1, e.sellSeatRearPos("10203040A", "Alice", 34, false)); //venta normal
		Assert.assertEquals(-1, ep.sellSeatRearPos("10203040A", "Alice", 34, true));//venta anticipada
	}

	@Test
	public void testsellSeatRearLleno() throws Exception{
		Assert.assertEquals(4, ep.sellSeatRearPos("10203040A","Alice", 34,true));	//venta anticipada
		Assert.assertEquals(3, ep.sellSeatRearPos("10203040B", "Alice", 34, false)); //venta normal
		Assert.assertEquals(2, ep.sellSeatRearPos("10203040C", "Alice", 34, false)); //venta normal
		Assert.assertEquals(1, ep.sellSeatRearPos("10203040D", "Alice", 34, true)); //venta anticipada
		Assert.assertEquals(-1, ep.sellSeatRearPos("10203040E", "Alice", 34, false)); //venta normal 
	}


	//TEST OF GET COLLECTION
	 
	@Test
	public void testgetCollectionAnticipadaYnormal() throws Exception{
		Assert.assertEquals(true, e.sellSeatPos(1, "1010", "AA", 10, true));
		Assert.assertEquals(true, e.sellSeatPos(4, "10101", "AA", 10, false));
		
		Assert.assertTrue(e.getCollectionTravel()==175.0);					
	}
	
	// TEST List
	@Test
	public void testGetListEventoCompleto() throws Exception{		
		Assert.assertEquals(true, ep.sellSeatPos(1, "10203040A","Alice", 34,true));	//venta normal  
		Assert.assertEquals(true, ep.sellSeatPos(2, "10203040B","Alice", 34,true));	//venta normal  
		Assert.assertEquals(true, ep.sellSeatPos(3, "10203040C","Alice", 34,false));	//venta normal  
		Assert.assertEquals(true, ep.sellSeatPos(4, "10203040D","Alice", 34,false));	//venta normal  
		Assert.assertEquals("[]", ep.getAvailableSeatsList().toString());
		Assert.assertEquals("[1, 2]", ep.getAdvanceSaleSeatsList().toString());
	}

	@Test
	public void testGetListEventoConPosicionesVacias(){
		Assert.assertEquals(true, ep.sellSeatPos(1, "10203040A","Alice", 34,false));	//venta normal  
		Assert.assertEquals(true, ep.sellSeatPos(2, "10203040B","Alice", 34,false));	//venta normal  
		Assert.assertEquals(true, ep.sellSeatPos(4, "10203040D","Alice", 34,false));	//venta normal 
		Assert.assertEquals("[3]", ep.getAvailableSeatsList().toString());
		Assert.assertEquals("[]", ep.getAdvanceSaleSeatsList().toString()); 
	}
	
	
	
	//TEST DE GETPRICE
	
	@Test
	public void testgetPrice() throws Exception{
		Assert.assertEquals(true,e.sellSeatPos(1, "1010", "AA", 10, true));
		Assert.assertEquals(true,e.sellSeatPos(4, "10101", "AA", 10, false));
		Assert.assertEquals(100.0,0.0,e.getSeatPrice(e.getSeat(4)));
		Assert.assertEquals(75.0,0.0,e.getSeatPrice(e.getSeat(1)));
		}
	
	
	//tests REFUND 
	
		
		@Test
		public void testREFUNDCero() throws Exception{
			Assert.assertEquals(true,e.sellSeatPos(1, "1010", "AA", 10, true));	
			Assert.assertEquals(null,e.refundSeat(0));
			}
		
		
		@Test
		public void testrefundOk() throws Exception{
			Person p=new Person("1010", "AA",10);
			Assert.assertEquals(true, e.sellSeatPos(1, "1010", "AA", 10, true));	
			Assert.assertEquals(p,e.refundSeat(1));
			}

		@Test
		public void testRefundFueraDeRango() throws Exception{
			Assert.assertEquals(null, e.refundSeat(111));
			Assert.assertEquals(null, e.refundSeat(-1));	
		}

		@Test
		public void testRefundPosicionesExtremos() throws Exception{
			Person p=new Person("1010", "AA",10);
			Assert.assertEquals(true, e.sellSeatPos(110, "1010", "AA", 10, true));
			Assert.assertEquals(p, e.refundSeat(110));
		
		}


		@Test
		public void testRefundPosicionIntermedia() throws Exception{
			Person p=new Person("1010", "AA",10);
			Assert.assertEquals(true, e.sellSeatPos(57, "1010", "AA", 10, true));	
			Assert.assertEquals(p,e.refundSeat(57));
		}

		@Test
		public void testRefundPosicionVacia() throws Exception{
			Assert.assertEquals(true,ep.sellSeatPos(1, "1010", "AA", 10, true));
			Assert.assertEquals(true,ep.sellSeatPos(2, "10101", "AA", 10, false));
			Assert.assertEquals(true,ep.sellSeatPos(4, "101010", "AA", 10, true));

			Assert.assertEquals(null, ep.refundSeat(3));
		}

		@Test
		public void testRefundSuccess() throws Exception{
			Person p = new Person("12345678A", "Juan", 30);
        	Seat s = new Seat(false, p);
			ep.sellSeatPos(2, "12345678A", "Juan", 30, false);
			Assert.assertEquals(p, s.getHolder());
			Assert.assertEquals(p, ep.refundSeat(2));
		}

		
		
	// TEST GetPosPerson
	@Test
		public void testGetPosPersonLleno() throws Exception{		
			   Assert.assertEquals(true,ep.sellSeatPos(1, "10203040","Alic", 34,true));	//venta anticipada  
			   Assert.assertEquals(true,ep.sellSeatPos(3, "10203040A","Alice", 34,false));	//venta normal  
			   Assert.assertEquals(true,ep.sellSeatPos(4, "10203040B","Alice", 34,false));	//venta normal  
			   Assert.assertEquals(-1,ep.getPosPerson("10205040"));
			   Assert.assertEquals(false,ep.isAdvanceSale(new Person("10203040A","Alice", 34)));
			   Assert.assertEquals(true,ep.isAdvanceSale(new Person("10203040","Alic", 34)));
			   Assert.assertEquals(false,ep.isAdvanceSale(new Person("10202531", "Ana", 31)));
			   Assert.assertEquals(3,ep.getPosPerson("10203040A"));
					 
		}
		
		
	// TEST OF getMaxNumberConsecutiveSeats
	@Test
		public void testGetMaxNumberConsecutiveSeats() throws Exception{
			Assert.assertEquals(true, ep.sellSeatPos(1, "1010", "AA", 10, true)); //venta anticipada
			Assert.assertEquals(true, ep.sellSeatPos(4, "10101", "AA", 10, false)); //venta normal
			Assert.assertEquals(2, ep.getMaxNumberConsecutiveSeats());

		}
	
	@Test
		public void testGetMaxNumberConsecutiveSeatsLleno() throws Exception{
			Assert.assertEquals(1 , ep.sellSeatFrontPos("10203040A","Alice", 34,true));	//venta anticipada
			Assert.assertEquals(2, ep.sellSeatFrontPos("10203040B", "Alice", 34, false)); //venta normal
			Assert.assertEquals(3, ep.sellSeatFrontPos("10203040C", "Alice", 34, false)); //venta normal
			Assert.assertEquals(4, ep.sellSeatFrontPos("10203040D", "Alice", 34, true)); //venta anticipada
			Assert.assertEquals(0, ep.getMaxNumberConsecutiveSeats());
		}

	@Test
		public void testGetMaxNumberConsecutiveSeatsEspacioFinal() throws Exception{
			Assert.assertEquals(true, ep.sellSeatPos(1, "1010", "AA", 10, true)); //venta anticipada
			Assert.assertEquals(true, ep.sellSeatPos(3, "10101", "AA", 10, false)); //venta normal
			Assert.assertEquals(true, ep.sellSeatPos(2, "101011", "AA", 10, true)); //venta anticipada
			Assert.assertEquals(1, ep.getMaxNumberConsecutiveSeats());
		}

	// TEST OF getNumberOfChildren
	@Test
		public void testGetNumberOfChildren() throws Exception{
			Assert.assertEquals(true, ep.sellSeatPos(1, "1010", "AA", 10, true)); //venta anticipada
			Assert.assertEquals(true, ep.sellSeatPos(3, "10101", "AA", 10, false)); //venta normal
			Assert.assertEquals(true, ep.sellSeatPos(2, "101011", "AA", 10, true)); //venta anticipada
			Assert.assertEquals(3, ep.getNumberOfChildren());
		}
	
	// TEST OF getNumberOfAdvanceSaleSeats
	@Test
		public void testgetNumberOfAdvanceSaleSeats() throws Exception{
			Assert.assertEquals(true, ep.sellSeatPos(1, "1010", "AA", 17, true)); //venta anticipada
			Assert.assertEquals(true, ep.sellSeatPos(3, "10101", "AA", 10, false)); //venta normal
			Assert.assertEquals(true, ep.sellSeatPos(2, "101011", "AA", 30, true)); //venta anticipada
			Assert.assertEquals(2, ep.getNumberOfAdvanceSaleSeats());
			Assert.assertEquals(1, ep.getNumberOfAdults());
			Assert.assertEquals(2, ep.getNumberOfChildren());
		}

	//TEST OF getNumberOfNormalSaleSeats y getNumberOfAdvanceSaleSeats
	@Test
		public void testGetNumberOfSeats() throws Exception{
			Assert.assertEquals(true, ep.sellSeatPos(1, "1010", "AA", 10, true)); //venta anticipada
			Assert.assertEquals(true, ep.sellSeatPos(3, "10101", "AA", 10, false)); //venta normal
			Assert.assertEquals(true, ep.sellSeatPos(2, "101011", "AA", 30, true)); //venta anticipada
			Assert.assertEquals(1, ep.getNumberOfNormalSaleSeats());
			Assert.assertEquals(2, ep.getNumberOfAdvanceSaleSeats());
		}

	@Test
		public void testGetNumberOfSeatsVacío() throws Exception{
			Assert.assertEquals(0, ep.getNumberOfAdvanceSaleSeats());
			Assert.assertEquals(0, ep.getNumberOfNormalSaleSeats());
		}

	@Test
		public void testGetNumberOfNormalSeatsTodasVentaAnticipada() throws Exception{
			Assert.assertEquals(true, ep.sellSeatPos(1, "1010", "AA", 10, true)); //venta anticipada
			Assert.assertEquals(true, ep.sellSeatPos(3, "10101", "AA", 10, true)); //venta normal
			Assert.assertEquals(true, ep.sellSeatPos(2, "101011", "AA", 30, true)); //venta anticipada
			Assert.assertEquals(0, ep.getNumberOfNormalSaleSeats());
		}

	//TEST OF getSeat
	@Test
		public void testGetSeatCero(){
			Assert.assertEquals(true,e.sellSeatPos(1, "1010", "AA", 10, true));
			Assert.assertEquals(true,e.sellSeatPos(4, "10101", "AA", 10, false));
			Assert.assertEquals(null, e.getSeat(78));
		}

	@Test
		public void testGetSeatUltimaPos() throws Exception{
			Assert.assertEquals(true,e.sellSeatPos(56, "1010", "AA", 10, true));
			Assert.assertEquals(true,e.sellSeatPos(110, "10101", "AA", 10, false));
			Assert.assertNotNull(e.getSeat(110));
		}
	
	/*@Test
		public void testGeatSeatPosIntermedia() throws Exception{
			//Person p=new Person("1010", "AA",10);
			//Seat s = new Seat(false, p);
			Assert.assertEquals(true,e.sellSeatPos(56, "1010", "AA", 10, false));
			Assert.assertEquals(new Seat(false, new Person("1010", "AA",10)), e.getSeat(56));
		}*/

	@Test
		public void testGeatSeatPosCero() throws Exception{
			//Assert.assertEquals(true,e.sellSeatPos(1, "1010", "AA", 10, true));	
			Assert.assertEquals(null,e.getSeat(0));
		}

	@Test
		public void testGetSeatPosUno() throws Exception{
			Assert.assertEquals(true,e.sellSeatPos(1, "1010", "AA", 10, true));
			Assert.assertNotNull(e.getSeat(1));
		}

	@Test
		public void testGetSeatPosicionIntermedia() throws Exception{
			Assert.assertEquals(true,e.sellSeatPos(10, "1010", "AA", 10, true));
			Assert.assertNotNull(e.getSeat(10));
		}

	@Test
		public void testGeatSeatPosicionFueraDeRango() throws Exception{
			Assert.assertEquals(null, e.getSeat(111));
		}

	// TEST OF equals
	@Test
		public void testEqualsReflexive() throws Exception{
        	Person person = new Person("10203040A", "Alice", 30);
        	Assert.assertTrue(person.equals(person)); // Reflexive: un objeto debe ser igual a sí mismo
    	}

	@Test
    	public void testEqualsNull() {
        	Person person = new Person("10203040A", "Alice", 30);
        	Assert.assertFalse(person.equals(null)); // Un objeto nunca debe ser igual a null
    	}

	@Test
    	public void testEqualsDistintaClass() {
        	Person person = new Person("10203040A", "Alice", 30);
        	Assert.assertFalse(person.equals("10203040A")); // Comparar con un objeto de otra clase
    	}
	
	@Test
    	public void testEqualsMismoNif() {
        	Person person1 = new Person("10203040A", "Alice", 30);
        	Person person2 = new Person("10203040A", "Ana", 25);
        	Assert.assertTrue(person1.equals(person2)); // Dos personas con el mismo NIF deben ser iguales
    	}

	@Test
    	public void testEqualsDistintoNif() {
        	Person person1 = new Person("10203040A", "Alice", 30);
        	Person person2 = new Person("10203040B", "Ana", 25);
        	Assert.assertFalse(person1.equals(person2)); // Dos personas con diferente NIF no deben ser iguales
    	}

}


