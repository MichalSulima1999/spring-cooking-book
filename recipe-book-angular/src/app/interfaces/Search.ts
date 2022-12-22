export interface Search {
  searchCriteriaList?: {
    filterKey: string;
    value: string;
    operation: string;
  }[];
  dataOption: string;
  orderByField: string;
  orderByAscending: boolean;
}
