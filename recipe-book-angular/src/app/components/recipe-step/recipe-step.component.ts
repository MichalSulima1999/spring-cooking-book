import { Component, Input } from '@angular/core';
import { RecipeStep } from 'src/app/interfaces/RecipeStep';

@Component({
  selector: 'app-recipe-step',
  templateUrl: './recipe-step.component.html',
  styleUrls: ['./recipe-step.component.css'],
})
export class RecipeStepComponent {
  @Input() step!: RecipeStep;
}
