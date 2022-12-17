import { Component, OnInit } from '@angular/core';
import { Validators, FormBuilder, FormArray, FormGroup } from '@angular/forms';
import { CdkDragDrop } from '@angular/cdk/drag-drop';
import {
  debounceTime,
  distinctUntilChanged,
  filter,
  finalize,
  switchMap,
  tap,
} from 'rxjs/operators';
import { IngredientService } from 'src/app/services/ingredient.service';
import { Ingredient } from 'src/app/interfaces/Ingredient';
import { StepService } from 'src/app/services/step.service';
import { Step } from 'src/app/interfaces/Step';
import { CategoryService } from 'src/app/services/category.service';
import { DietService } from 'src/app/services/diet.service';
import { Category } from 'src/app/interfaces/Category';
import { Diet } from 'src/app/interfaces/diet';
import { RecipeService } from 'src/app/services/recipe.service';
import { RecipeToAdd } from 'src/app/interfaces/RecipeToAdd';
import { Recipe } from 'src/app/interfaces/Recipe';
import { MatSnackBar } from '@angular/material/snack-bar';
import { TranslateService } from '@ngx-translate/core';
import { RecipeAddedSnackbarComponent } from 'src/app/components/recipe-added-snackbar/recipe-added-snackbar.component';

@Component({
  selector: 'app-add-recipe',
  templateUrl: './add-recipe.component.html',
  styleUrls: ['./add-recipe.component.css'],
})
export class AddRecipeComponent implements OnInit {
  recipeForm = this.fb.group({
    recipe: this.fb.group({
      name: ['', [Validators.required]],
      description: ['', [Validators.required]],
      cookingMinutes: [
        '',
        [
          Validators.pattern('^[0-9]*$'),
          Validators.min(1),
          Validators.required,
        ],
      ],
      skillLevel: ['', [Validators.required]],
    }),
    category: this.fb.group({
      name: ['', [Validators.required]],
    }),
    diet: this.fb.group({
      name: ['', [Validators.required]],
    }),
    ingredientQuantityDtos: this.fb.array([]),
    stepNumberDtos: this.fb.array([]),
  });

  recipe: RecipeToAdd = {
    recipe: {
      name: '',
      description: '',
      cookingMinutes: 0,
      skillLevel: '',
    },
    category: {
      name: '',
    },
    diet: {
      name: '',
    },
    ingredientQuantityDtos: [{ ingredientName: '', quantity: '' }],
    stepNumberDtos: [{ stepDescription: '', stepNumber: 1 }],
  };

  filteredIngredients!: any;
  minIngredientNameLengthSearch: number = 3;
  ingredientSearchError: string = '';
  ingredientSearchLoading: boolean = true;

  filteredSteps!: any;
  minStepDescriptionLengthSearch: number = 5;
  stepSearchError: string = '';
  stepSearchLoading: boolean = true;

  filteredCategories!: any;
  minCategoryDescriptionLengthSearch: number = 3;
  categorySearchError: string = '';
  categorySearchLoading: boolean = true;

  filteredDiets!: any;
  minDietDescriptionLengthSearch: number = 3;
  dietSearchError: string = '';
  dietSearchLoading: boolean = true;

  constructor(
    private fb: FormBuilder,
    private ingredientService: IngredientService,
    private stepService: StepService,
    private categoryService: CategoryService,
    private dietService: DietService,
    private recipeService: RecipeService,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit() {
    this.addIngredientQuantityDto();
    this.addStepNumberDto();

    this.category.controls['name'].valueChanges
      .pipe(
        filter((res) => {
          return (
            res !== null &&
            res.length >= this.minCategoryDescriptionLengthSearch
          );
        }),
        distinctUntilChanged(),
        debounceTime(1000),
        tap(() => {
          this.categorySearchError = '';
          this.filteredCategories = [];
          this.categorySearchLoading = true;
        }),
        switchMap((value) =>
          this.categoryService.getCategoryHints(value!).pipe(
            finalize(() => {
              this.categorySearchLoading = false;
            })
          )
        )
      )
      .subscribe((data: Category[]) => {
        this.filteredCategories = data.map((category) => category.name);
        console.log(this.filteredCategories);
      });

    this.diet.controls['name'].valueChanges
      .pipe(
        filter((res) => {
          return (
            res !== null && res.length >= this.minDietDescriptionLengthSearch
          );
        }),
        distinctUntilChanged(),
        debounceTime(1000),
        tap(() => {
          this.dietSearchError = '';
          this.filteredDiets = [];
          this.dietSearchLoading = true;
        }),
        switchMap((value) =>
          this.dietService.getDietHints(value!).pipe(
            finalize(() => {
              this.dietSearchLoading = false;
            })
          )
        )
      )
      .subscribe((data: Diet[]) => {
        this.filteredDiets = data.map((diet) => diet.name);
        console.log(this.filteredDiets);
      });
  }

  get category() {
    return this.recipeForm.controls['category'] as FormGroup;
  }

  get diet() {
    return this.recipeForm.controls['diet'] as FormGroup;
  }

  get ingredientQuantityDtos() {
    return this.recipeForm.controls['ingredientQuantityDtos'] as FormArray;
  }

  addIngredientQuantityDto() {
    const ingredientQuantityDtoForm = this.fb.group({
      ingredientName: ['', [Validators.required]],
      quantity: ['', [Validators.required]],
    });

    ingredientQuantityDtoForm.controls['ingredientName'].valueChanges
      .pipe(
        filter((res) => {
          return (
            res !== null && res.length >= this.minIngredientNameLengthSearch
          );
        }),
        distinctUntilChanged(),
        debounceTime(1000),
        tap(() => {
          this.ingredientSearchError = '';
          this.filteredIngredients = [];
          this.ingredientSearchLoading = true;
        }),
        switchMap((value) =>
          this.ingredientService.getIngredientHints(value!).pipe(
            finalize(() => {
              this.ingredientSearchLoading = false;
            })
          )
        )
      )
      .subscribe((data: Ingredient[]) => {
        this.filteredIngredients = data.map((ingredient) => ingredient.name);
        console.log(this.filteredIngredients);
      });

    this.ingredientQuantityDtos.push(ingredientQuantityDtoForm);
  }

  deleteIngredientQuantityDto(ingredientIndex: number) {
    this.ingredientQuantityDtos.removeAt(ingredientIndex);
  }

  get stepNumberDtos() {
    return this.recipeForm.controls['stepNumberDtos'] as FormArray;
  }

  addStepNumberDto() {
    const stepNumberDtoForm = this.fb.group({
      stepDescription: ['', [Validators.required]],
      stepNumber: [this.stepNumberDtos.length + 1, [Validators.required]],
    });

    stepNumberDtoForm.controls['stepDescription'].valueChanges
      .pipe(
        filter((res) => {
          return (
            res !== null && res.length >= this.minStepDescriptionLengthSearch
          );
        }),
        distinctUntilChanged(),
        debounceTime(1000),
        tap(() => {
          this.stepSearchError = '';
          this.filteredSteps = [];
          this.stepSearchLoading = true;
        }),
        switchMap((value) =>
          this.stepService.getStepHints(value!).pipe(
            finalize(() => {
              this.stepSearchLoading = false;
            })
          )
        )
      )
      .subscribe((data: Step[]) => {
        this.filteredSteps = data.map((step) => step.description);
        console.log(this.filteredSteps);
      });

    this.stepNumberDtos.push(stepNumberDtoForm);
  }

  deleteStepNumberDto(stepIndex: number) {
    this.stepNumberDtos.removeAt(stepIndex);
  }

  drop(event: CdkDragDrop<string[]>) {
    const stepTmp = this.stepNumberDtos.at(event.previousIndex);
    this.removeStep(event.previousIndex);
    this.stepNumberDtos.insert(event.currentIndex, stepTmp);
    this.stepNumberDtos.controls.map((element, index) => {
      element.value['stepNumber'] = index + 1;
    });
  }

  removeStep(index: number) {
    this.stepNumberDtos.removeAt(index);
  }

  onSubmit() {
    if (this.recipeForm.invalid) return;
    console.log(this.recipeForm.value);
    console.log(this.recipe);
    Object.assign(this.recipe, this.recipeForm.value);

    this.recipeService
      .addRecipe(this.recipe)
      .subscribe((recipe: Recipe) => this.openSnackBar(recipe.id!));
    console.log(this.recipeForm.value);
  }

  openSnackBar(recipeId: number) {
    this.snackBar.openFromComponent(RecipeAddedSnackbarComponent, {
      data: recipeId,
    });
  }
}
