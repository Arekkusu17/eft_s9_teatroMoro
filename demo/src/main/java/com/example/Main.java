/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

 package com.example;
 import java.util.ArrayList;
 import java.util.List;
 import java.util.UUID;
 import java.util.Scanner;
 
 /**
  *
  * @author Alex Fernández
  */
 public class Main {
     static Scanner scanner = new Scanner(System.in);
 
     // Constantes
     static final int MAX_SALES = 100;
     static final String[] ROW_NAMES = {"vip", "palco", "platea baja", "platea alta", "galería"};
     static final double[] BASE_PRICES = {20000.0, 18000.0, 15000.0, 12000.0, 10000.0};

     static final int COLUMNS = 10;
 
     // Listas
     static List<String> discounts = new ArrayList<>();
     static List<Reservation> reservations = new ArrayList<>();
 
     // Arreglos
     static Customer[] customers = new Customer[MAX_SALES]; // Arreglo de clientes
     static int[] saleIds = new int[MAX_SALES]; // Arreglo de IDs de ventas
     static boolean[][] seatMap = new boolean[ROW_NAMES.length][COLUMNS]; // Arreglo para gestionar los asientos
 
     // Variables auxiliares
     static int saleIndex = 0;
 
     // Clase para representar una reserva
     static class Reservation {
         int reservationId;
         int customerId; // ID del cliente relacionado
         String customerName; // Nombre del cliente
         String discountType; // Tipo de descuento aplicado
         int row;
         int column;
         double finalPrice;
 
         public Reservation(int reservationId, int customerId, String customerName, String discountType, int row, int column, double finalPrice) {
             this.reservationId = reservationId;
             this.customerId = customerId;
             this.customerName = customerName;
             this.discountType = discountType;
             this.row = row;
             this.column = column;
             this.finalPrice = finalPrice;
         }
 
         @Override
         public String toString() {
            return "Reserva #" + reservationId + ": Cliente " + customerName + " (ID: " + customerId + "), Descuento: " + discountType +
                   ", Asiento [" + ROW_NAMES[row] + "," + column + "], Precio final: $" + finalPrice;
        }
     }
 
     // Clase para representar un cliente
     static class Customer {
         int id;
         String name;
         String discountType;
 
         public Customer(int id, String name, String discountType) {
             this.id = id;
             this.name = name;
             this.discountType = discountType;
         }
 
         public String getName() {
             return name;
         }
 
         public int getId() {
             return id;
         }
 
         public String getDiscountType() {
             return discountType;
         }
     }
 
     public static void main(String[] args) {
         initializeDiscounts(); // Inicializamos la lista de descuentos
         showMenu();
     }
 
     static void initializeDiscounts() {
        // EXPLICACIÓN DE LAS CATEGORIAS DE DESCUENTO PARA ESTE CASO DE USO
        // "niños": Descuento del 10% para toda persona menor a 18 años y que no es estudiante.
        // "estudiante": Descuento del 15% para las personas que son estudiantes.
        // "mujeres": Descuento del 20% para las personas que son mujeres, mayores a 18 años, que no son estudiantes ni pertenecen a la tercera edad.
        // "tercera edad": Descuento del 25% para las personas que son de la tercera edad (mayores de 65 años).
        // 
         discounts.add("niños:0.10");
         discounts.add("estudiante:0.15");
         discounts.add("mujeres:0.20");
         discounts.add("tercera edad:0.25");
         discounts.add("ninguno:0.0");
     }
 
     static void showMenu() {
         int option;
         do {
             System.out.println("\n--- Teatro Moro - Sistema Optimizado ---");
             System.out.println("1. Vender entrada");
             System.out.println("2. Mostrar reservas");
             System.out.println("3. Eliminar reserva");
             System.out.println("4. Actualizar asiento de reserva");
             System.out.println("5. Salir");
             option = getValidOption(1, 5);
             switch (option) {
                 case 1:
                     sellTicket();
                     break;
                 case 2:
                     showReservations();
                     break;
                 case 3:
                     deleteReservation();
                     break;
                 case 4:
                     updateReservation();
                     break;
                 case 5:
                     displaySeatMap();
                     break;
                 default:
                     System.out.println("Opción inválida.");
             }
         } while (option != 0);
     }
 
     static void sellTicket() {
         if (saleIndex >= MAX_SALES) {
             System.out.println("Se alcanzó el límite de ventas.");
             return;
         }
 
         System.out.print("Ingrese el nombre del cliente: ");
         String name = scanner.nextLine();

         int ageClient = getValidAge();
         
         System.out.print("El cliente es hombre, mujer o prefiere no informar: (hombre/mujer/no informar): ");
         String genderClient = getValidGender();

 
         System.out.print("¿Es estudiante/tercera edad/ninguno? (Ingresar solo una categoría): ");
         String discountType = getValidDiscountType(ageClient, genderClient);
 
         int row = getValidRow();
 
         int column = getValidSeat();
 
         if (seatMap[row][column]) {
             System.out.println("El asiento ya está ocupado.");
             return;
         }
 
         double discount = calculateDiscount(discountType);
         double basePrice = BASE_PRICES[row];
         double finalPrice = basePrice - (basePrice * discount); 
         // Crear nuevo cliente
         Customer customer = new Customer(saleIndex + 1, name, discountType);
         customers[saleIndex] = customer;
 
         // Crear nueva reserva
         Reservation reservation = new Reservation(saleIndex + 1, customer.getId(), customer.getName(), discountType, row, column, finalPrice);
         reservations.add(reservation);
 
         // Marcar el asiento como ocupado
         seatMap[row][column] = true;
 
         // Guardar ID de la venta
         saleIds[saleIndex] = customer.getId();
 
         System.out.println("¡Entrada vendida con éxito!");
         System.out.println(reservation);
 
         saleIndex++;
     }
 
     static double calculateDiscount(String discountType) {
         // Buscar el tipo de descuento en la lista
         for (String discount : discounts) {
             String[] parts = discount.split(":");
             if (parts[0].equals(discountType)) {
                 return Double.parseDouble(parts[1]);
             }
         }
         return 0.0; // Si no encuentra, no hay descuento
     }
 
     static void showReservations() {
         if (reservations.isEmpty()) {
             System.out.println("No hay reservas registradas.");
         } else {
             for (Reservation reservation : reservations) {
                 System.out.println(reservation);
             }
         }
     }
 
     static void deleteReservation() {
         System.out.print("Ingrese el ID de la reserva a eliminar: ");
         int id = scanner.nextInt();
         scanner.nextLine();
 
         Reservation r = findReservationById(id);
         if (r != null) {
             // Liberar el asiento
             seatMap[r.row][r.column] = false;
             // Eliminar el cliente de la lista de clientes
             customers[r.customerId - 1] = null;  // Eliminar el cliente del arreglo (el cliente tiene un ID basado en el índice de venta)
             
             // Eliminar el ID de venta del arreglo saleIds
             saleIds[r.customerId - 1] = 0;  // Eliminar el ID de venta
 
             // Eliminar la reserva de la lista de reservas
             reservations.remove(r);
             System.out.println("Reserva eliminada correctamente.");
         } else {
             System.out.println("No se encontró una reserva con ese ID.");
         }
     }
 
     static void updateReservation() {
         System.out.print("Ingrese el ID de la reserva a actualizar: ");
         int id = scanner.nextInt();
         scanner.nextLine();
 
         Reservation r = findReservationById(id);
         if (r != null) {
             // Liberar el asiento anterior
             seatMap[r.row][r.column] = false;
 
             int newRow = getValidRow();
             int newColumn = getValidSeat();
 
             if (seatMap[newRow][newColumn]) {
                 System.out.println("El nuevo asiento ya está ocupado.");
                 return;
             }
 
             // Asignar el nuevo asiento
             r.row = newRow;
             r.column = newColumn;
             seatMap[newRow][newColumn] = true;
 
             System.out.println("Reserva actualizada correctamente.");
         } else {
             System.out.println("No se encontró una reserva con ese ID.");
         }
     }
 
     // UTILIDADES
     static void displaySeatMap() {
         System.out.println("\nMapa de asientos (X = ocupado, O = libre):");
         for (int i = 0; i < seatMap.length; i++) {
            System.out.printf("%-13s: ", ROW_NAMES[i]); // Alineación fija para nombres de fila
            for (int j = 0; j < seatMap[i].length; j++) {
                System.out.print(seatMap[i][j] ? "X " : "O ");
             }
             System.out.println();
         }
     }
 
     static Reservation findReservationById(int id) {
         for (Reservation r : reservations) {
             if (r.reservationId == id) {
                 return r;
             }
         }
         return null;
     }
 
     static int getValidRow() {
        int row = -1;
        while (row == -1) {
            System.out.print("Seleccione fila (vip, palco, platea baja, platea alta, galeria): ");
            String input = scanner.nextLine().toLowerCase();
            // Verifica si la entrada es un nombre de fila válido, y retorna el índice correspondiente (0-4)
            row = getRowIndexByName(input);
            if (row == -1) {
                System.out.println("Nombre de fila inválido.");
            }
        }
        return row;
     }
 
     static int getValidSeat() {
         int seat = -1;
         boolean validSeat = false;
 
         while (!validSeat) {
             System.out.print("Seleccione asiento (0-9): ");
             if (scanner.hasNextInt()) {
                 seat = scanner.nextInt();
                 scanner.nextLine();
 
                 if (seat >= 0 && seat < 10) {
                     validSeat = true;
                 } else {
                     System.out.println("Asiento fuera de rango.");
                 }
             } else {
                 System.out.println("Entrada inválida. Ingrese un número: ");
                 scanner.nextLine(); // Limpiar el buffer
             }
         }
         return seat;
     }
 
     static String  getValidDiscountType(int age, String gender) {
        String inputDiscount;

        while (true) {
            System.out.print("¿Es estudiante/tercera edad/ninguno? (Ingresar solo una categoría): ");
            inputDiscount = scanner.nextLine().toLowerCase();
    
            if (inputDiscount.equals("estudiante") || inputDiscount.equals("tercera edad") || inputDiscount.equals("ninguno")) {
                break;
            } else {
                System.out.println("Tipo de descuento inválido. Ingrese 'estudiante', 'tercera edad' o 'ninguno'.");
            }
        }
    
        return determineDiscountType(age, gender, inputDiscount);
     }

    static String determineDiscountType(int age, String gender, String inputDiscount) {
        String baseDiscount = inputDiscount.toLowerCase();
    
        if (baseDiscount.equals("estudiante")) {
            return "estudiante";
        }
    
        if (baseDiscount.equals("tercera edad") && age >= 65) {
            return "tercera edad";
        }
    
        if (age < 18 && !baseDiscount.equals("estudiante")) {
            return "niños";
        }
    
        if (age > 18 && gender.equals("mujer") && !baseDiscount.equals("estudiante") && age < 65) {
            return "mujeres";
        }
    
        return "ninguno";
    }
    


 
     static int getValidOption(int min, int max) {
         int option = -1;
         boolean validOption = false;
 
         while (!validOption) {
             System.out.print("Seleccione una opción (" + min + "-" + max + "): ");
             if (scanner.hasNextInt()) {
                 option = scanner.nextInt();
                 scanner.nextLine(); 
                 if (option >= min && option <= max) {
                     validOption = true;
                 } else {
                     System.out.println("Opción fuera de rango.");
                 }
             } else {
                 System.out.println("Entrada inválida. Ingrese un número.");
                 scanner.nextLine(); 
             }
         }
 
         return option;
     }

     // Método para obtener una edad válida
    static int getValidAge(){
        // Se utiliza un bucle while para seguir pidiendo la edad hasta que se ingrese un valor válido
        int age = 0;
        boolean validAge = false;
        while (!validAge) {
            System.out.print("Ingresa edad del cliente: ");
            if (scanner.hasNextInt()) {
                age = scanner.nextInt();
                scanner.nextLine(); 

                if (age > 0) {
                    validAge = true; // Edad válida
                } else {
                    System.out.println("\nEdad no válida. Debe ser un número entero mayor a cero.");
                }
            } else {
                System.out.println("\nEdad no válida. Por favor, ingresa un número entero mayor a cero.");
                scanner.nextLine(); 
            }
        }

        return age;
    }

    static String getValidGender() {
        String gender = scanner.nextLine().toLowerCase();
        while (!gender.equals("hombre") && !gender.equals("mujer") && !gender.equals("no informar")) {
            System.out.print("Opción no válida. Ingrese 'hombre', 'mujer' o 'no informar': ");
            gender = scanner.nextLine().toLowerCase();            
        }
        return gender;
    }

     static int getRowIndexByName(String name) {
        for (int i = 0; i < ROW_NAMES.length; i++) {
            if (ROW_NAMES[i].equalsIgnoreCase(name)) {
                return i;
            }
        }
        //No se encontró la fila
        return -1; 
    }
 }