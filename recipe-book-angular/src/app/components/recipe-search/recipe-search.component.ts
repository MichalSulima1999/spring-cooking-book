import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { Validators, FormBuilder } from '@angular/forms';
import { Category } from 'src/app/interfaces/Category';
import { Diet } from 'src/app/interfaces/diet';
import { Search } from 'src/app/interfaces/Search';
import { CategoryService } from 'src/app/services/category.service';
import { DietService } from 'src/app/services/diet.service';

@Component({
  selector: 'app-recipe-search',
  templateUrl: './recipe-search.component.html',
  styleUrls: ['./recipe-search.component.css'],
})
export class RecipeSearchComponent implements OnInit {
  @Output() onRetrieveRecipes: EventEmitter<Search> = new EventEmitter();

  searchForm = this.fb.group({
    category: [''],
    name: [''],
    diet: [''],
    skillLevel: [''],
    maxCookingMinutes: [
      '',
      [Validators.pattern('^[0-9]*$'), Validators.min(1)],
    ],
    sort: this.fb.group({
      orderByField: ['name', [Validators.required]],
      sortingOrder: ['ASC', [Validators.required]],
    }),
  });

  sortingOrders: string[] = ['ASC', 'DESC'];
  orderByFields: string[] = ['name', 'cookingMinutes'];

  categories!: Category[];
  diets!: Diet[];

  constructor(
    private fb: FormBuilder,
    private categoryService: CategoryService,
    private dietService: DietService
  ) {}

  ngOnInit(): void {
    this.retrieveCategories();
    this.retrieveDiets();

    this.searchForm.setValue({
      category: '',
      name: '',
      diet: '',
      skillLevel: '',
      maxCookingMinutes: '',
      sort: {
        orderByField: this.orderByFields.at(0)!,
        sortingOrder: this.sortingOrders.at(0)!,
      },
    });
  }

  retrieveCategories(): void {
    this.categoryService.getCategories().subscribe((categories: Category[]) => {
      this.categories = categories;
    });
  }

  retrieveDiets(): void {
    this.dietService.getDiets().subscribe((diets: Diet[]) => {
      this.diets = diets;
    });
  }

  onSubmit() {
    if (!this.searchForm.valid) return;

    let searchCriteriaList = [];

    if (this.searchForm.value.category) {
      searchCriteriaList.push({
        filterKey: 'category',
        value: this.searchForm.value.category!,
        operation: operations.Equals.valueOf(),
      });
    }

    if (this.searchForm.value.name) {
      searchCriteriaList.push({
        filterKey: 'name',
        value: this.searchForm.value.name!,
        operation: operations.Contains.valueOf(),
      });
    }

    if (this.searchForm.value.maxCookingMinutes) {
      searchCriteriaList.push({
        filterKey: 'cookingMinutes',
        value: this.searchForm.value.maxCookingMinutes!,
        operation: operations.LessEquals.valueOf(),
      });
    }

    if (this.searchForm.value.skillLevel) {
      searchCriteriaList.push({
        filterKey: 'skillLevel',
        value: this.searchForm.value.skillLevel!,
        operation: operations.Equals.valueOf(),
      });
    }

    if (this.searchForm.value.diet) {
      searchCriteriaList.push({
        filterKey: 'diet',
        value: this.searchForm.value.diet!,
        operation: operations.Equals.valueOf(),
      });
    }

    const search: Search = {
      searchCriteriaList: searchCriteriaList,
      dataOption: 'all',
      orderByField: this.searchForm.value.sort?.orderByField!,
      orderByAscending: this.searchForm.value.sort?.sortingOrder === 'ASC',
    };

    this.onRetrieveRecipes.emit(search);
  }
}

enum operations {
  Equals = 'eq',
  Contains = 'cn',
  LessEquals = 'le',
}
