package ule.edi.travel;



import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ule.edi.model.*;

public class TravelArrayImpl implements Travel {
	
	private static final Double DEFAULT_PRICE = 100.0;
	private static final Byte DEFAULT_DISCOUNT = 25;
	private static final Byte CHILDREN_EXMAX_AGE = 18;
	private Date travelDate;
	private int nSeats;
	
	private Double price;    // precio de entradas 
	private Byte discountAdvanceSale;   // descuento en venta anticipada (0..100)
   	
	private Seat[] seats;
		
	
	
   public TravelArrayImpl(Date date, int nSeats){
	   //TODO 
	   // utiliza los precios por defecto: DEFAULT_PRICE y DEFAULT_DISCOUNT definidos en esta clase
	   //debe crear el array de asientos
	   this.travelDate = date;
	   this.nSeats = nSeats;
	   this.price = DEFAULT_PRICE;
	   this.discountAdvanceSale = DEFAULT_DISCOUNT;
	   this.seats = new Seat[nSeats];
   }
   
   
   public TravelArrayImpl(Date date, int nSeats, Double price, Byte discount){
	   //TODO 
	   // Debe crear el array de asientos
	   this.travelDate = date;
	   this.nSeats = nSeats;
	   this.seats = new Seat[nSeats];
	   this.price = price;
	   this.discountAdvanceSale = discount;
   }






@Override
public Byte getDiscountAdvanceSale() {
	return discountAdvanceSale;
}


@Override
public int getNumberOfSoldSeats() {
	int contador = 0;
	for (int i = 0; i < this.seats.length; i ++){
		if(this.seats[i] != null){
			contador ++;
		}
	}
	return contador;
}


@Override
public int getNumberOfNormalSaleSeats() {
	int contador = 0;
	for (int i = 0; i < this.seats.length; i ++){
		if(this.seats[i] != null && !this.seats[i].getAdvanceSale()){
				contador ++;
		}
	}
	return contador;
}


@Override
public int getNumberOfAdvanceSaleSeats() {
	int contador = 0;
	for (int i = 0; i < this.seats.length; i ++){
		if(this.seats[i] != null && this.seats[i].getAdvanceSale()){
				contador ++;
		}
	}
	return contador;
}


@Override
public int getNumberOfSeats() {
	return this.nSeats;
}


@Override
public int getNumberOfAvailableSeats() {
	int availableSeats = this.getNumberOfSeats() - this.getNumberOfSoldSeats();
	return availableSeats;

}

@Override
public Seat getSeat(int pos) {
	if(pos >= 1 && pos <= this.seats.length && this.seats[pos - 1] != null){
		Seat seat = new Seat(this.seats[pos - 1].getAdvanceSale(), this.seats[pos -1].getHolder());
		return seat;
	}
	else{
		return null;
	}
}


@Override
public Person refundSeat(int pos) {
	if(pos >= 1 && pos <= this.seats.length && this.seats[pos - 1] != null){
			Person holder = this.seats[pos - 1].getHolder();
			this.seats[pos - 1] = null;
			return holder;
		
	}else{
		return null;
	}
}



private boolean isChildren(int age) {
	boolean isChildren = false;
	if(age >= 0 && age < CHILDREN_EXMAX_AGE){
		isChildren = true;
	}
	return isChildren;
}

private boolean isAdult(int age) {
	boolean isAdult = false;
	if(age >= CHILDREN_EXMAX_AGE){
		isAdult = true;
	}
	return isAdult;
}


@Override
public List<Integer> getAvailableSeatsList() {

	List<Integer> lista=new ArrayList<Integer>(nSeats);
	for (int i = 0; i < this.seats.length; i ++){
		if(this.seats[i] == null){
			lista.add(i + 1); //Agrega la posición del asiento vacío (las posiciones del asiento comienzan en 1 y las del array en 1) a la lista
		}
	}
	
	return lista;
}


@Override
public List<Integer> getAdvanceSaleSeatsList() {
	List<Integer> lista=new ArrayList<Integer>(nSeats);
	for (int i = 0; i < this.seats.length; i ++){
		if(this.seats[i] != null && this.seats[i].getAdvanceSale()){
			lista.add(i + 1); //Agrega la posición del asiento vendido en venta anticipada (las posiciones del asiento comienzan en 1 y las del array en 1) a la lista
		}
	}
	
	return lista;
}


@Override
public int getMaxNumberConsecutiveSeats() {
	int maxConsecutive = 0;
	int currentConsecutive = 0;
	for (int i = 0; i < this.seats.length; i ++){
		if(this.seats[i] == null){
			currentConsecutive ++;
			maxConsecutive = Math.max(maxConsecutive, currentConsecutive);
		} else {
			currentConsecutive = 0;
		}
	}
	return maxConsecutive;
}


@Override
public boolean isAdvanceSale(Person p) {
	for (int i = 0; i < this.seats.length; i++){
		if(this.seats[i] != null && this.seats[i].getHolder().equals(p) && this.seats[i].getAdvanceSale()){
				return true;
		}
	}
	return false;
}


@Override
public Date getTravelDate() {
	return this.travelDate;
}


@Override
public boolean sellSeatPos(int pos, String nif, String name, int edad, boolean isAdvanceSale) {

	
	if(pos >= 1 && (pos - 1) < this.seats.length && this.seats[pos - 1] == null && getPosPerson(nif) == -1){
		Person person = new Person(nif, name, edad);
		Seat seat = new Seat(isAdvanceSale, person);
		this.seats[pos - 1] = seat;
		return true; //Se vendió el asiento con éxito
	}
	return false; //No se pudo vender el asiento

}


@Override
public int getNumberOfChildren() {
	int contador = 0;
	for (int i = 0; i < this.seats.length; i ++){
		if(this.seats[i] != null && isChildren(this.seats[i].getHolder().getAge())){
			contador ++;
		}

	}
	return contador;
}


@Override
public int getNumberOfAdults() {
	int contador = 0;
	for (int i = 0; i < this.seats.length; i ++){
		if(this.seats[i] != null && isAdult(this.seats[i].getHolder().getAge())){
			contador ++;
		}
	}
	
	return contador;
}



@Override
public Double getCollectionTravel() {
	double collection = 0.0;
	for (int i=0; i < this.seats.length; i++){
		if(this.seats[i] != null){
			collection += getSeatPrice(seats[i]);
		}
	}
	
	return collection;
}


@Override
public int getPosPerson(String nif) {
	for (int i = 0; i < this.seats.length; i ++){
		if(this.seats[i] != null && this.seats[i].getHolder().getNif().equals(nif)){
				return i + 1;
			}
		}
	return -1;	//En el caso de que no se encuentre la persona con el nif dado
}


@Override
public int sellSeatFrontPos(String nif, String name, int edad, boolean isAdvanceSale) {
	if(getPosPerson(nif) == -1){
		for (int i = 0; i < this.seats.length; i ++){
			if(this.seats[i] == null){
				Person person = new Person(nif, name, edad);
				Seat seat = new Seat(isAdvanceSale, person);
				this.seats[i] = seat;
				return i + 1; //Devuelve la posición del asiento, las posiciones del autobús comienzan en 1
			}
		}
	}
	return -1; //Devuelve -1 en el caso de que no se haya encontrado ningún asiento vacío
}


@Override
public int sellSeatRearPos(String nif, String name, int edad, boolean isAdvanceSale) {
	if(getPosPerson(nif) == -1){
		for(int i = this.seats.length - 1; i >= 0; i --){
			if(this.seats[i] == null){
				Person person = new Person(nif, name, edad);
				Seat seat = new Seat(isAdvanceSale, person);
				this.seats[i] = seat;
				return i + 1; //Devuelve la posición del asiento, las posiciones del autobús comienzan en 1
			}
		}
	}
	return -1; //Devuelve -1 en el caso de que no se haya encontrado ningún asiento vacío
}




@Override
public Double getSeatPrice(Seat seat) {
	Double seatPrice = this.price;
	if(seat.getAdvanceSale()){
		seatPrice = seatPrice * (100 - discountAdvanceSale) / 100.0;
	}

	return seatPrice;
}


@Override
public double getPrice() {
	return this.price;
}


}	