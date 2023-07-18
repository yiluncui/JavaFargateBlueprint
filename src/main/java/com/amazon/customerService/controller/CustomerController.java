@GetMapping("/customers/sorted")
List<Customer> getCustomersSortedByEmail(@RequestParam String sortOrder) {
    return customerService.findAllSortedByEmail(sortOrder);
}

/*
 * Existing code...
 */
